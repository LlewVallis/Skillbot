package org.astropeci.skillbot.request.match;

import de.gesundkrank.jskills.*;
import lombok.extern.slf4j.Slf4j;
import org.astropeci.skillbot.data.match.MatchOutcome;
import org.astropeci.skillbot.data.player.Player;
import org.astropeci.skillbot.data.player.Skill;
import org.astropeci.skillbot.data.player.SkillValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Applies the trueskill effect of a match to a player without storing or propagating the effect.
 */
@Slf4j
@Component
public class TrueskillAdjuster {

    @Value("${trueskill.default.trueskill}")
    private double defaultTrueskill;
    @Value("${trueskill.default.deviation}")
    private double defaultDeviation;

    @Value("${trueskill.trueskill-deviations-from-mean}")
    private double trueskillDeviationsFromMean;

    @Value("${trueskill.beta}")
    private double beta;
    @Value("${trueskill.tau}")
    private double tau;
    @Value("${trueskill.draw-probability}")
    private double drawProbability;

    private final SkillValidator skillValidator = new SkillValidator();

    public void adjustTrueskill(Set<Player> teamA, Set<Player> teamB, MatchOutcome outcome) throws UnbalancedTeamsException, DuplicatePlayerException {
        if (teamA.size() != teamB.size()) {
            throw new UnbalancedTeamsException();
        }

        checkNoDuplicates(teamA, teamB);

        GameInfo settings = new GameInfo(
                trueskillToMean(defaultTrueskill, defaultDeviation),
                defaultDeviation,
                beta,
                tau,
                drawProbability
        );

        ITeam teamARanks = createTeam(teamA);
        ITeam teamBRanks = createTeam(teamB);

        int[] placings = createPlacings(outcome);

        Map<IPlayer, Rating> newRatings;
        try {
            newRatings = TrueSkillCalculator.calculateNewRatings(
                    settings,
                    List.of(teamARanks, teamBRanks),
                    placings
            );
        } catch (Exception e) {
            log.error("Error computing result %s between %s and %s".formatted(
                    outcome, teamToString(teamA), teamToString(teamB)
            ), e);

            throw new IncalculableMatchException("exception while calculating match", e);
        }

        applyNewRatings(newRatings);
    }

    private void checkNoDuplicates(Set<Player> teamA, Set<Player> teamB) throws DuplicatePlayerException {
        Set<Player> foundPlayers = new HashSet<>();

        List<Player> bothTeams = Stream.concat(teamA.stream(), teamB.stream()).collect(Collectors.toList());

        for (Player player : bothTeams) {
            if (!foundPlayers.add(player)) {
                throw new DuplicatePlayerException(player);
            }
        }
    }

    private void applyNewRatings(Map<IPlayer, Rating> ratings) {
        for (Map.Entry<IPlayer, Rating> entry : ratings.entrySet()) {
            Player player = unwrapPlayer(entry.getKey());
            Rating rating = entry.getValue();

            Skill skill = new Skill(
                    meanToTrueskill(rating.getMean(), rating.getStandardDeviation()),
                    rating.getStandardDeviation()
            );

            if (!skillValidator.isValid(skill)) {
                throw new IncalculableMatchException("invalid skill after calculation");
            }

            if (skill.getDeviation() > defaultDeviation) {
                skill.setDeviation(defaultDeviation);
            }

            player.setSkill(skill);
        }
    }

    private int[] createPlacings(MatchOutcome outcome) {
        switch (outcome) {
            case TEAM_A_WINS:
                return new int[] { 1, 2 };
            case TIE:
                return new int[] { 1, 1 };
        }

        throw new IllegalStateException();
    }

    private Player unwrapPlayer(IPlayer player) {
        return (Player) ((de.gesundkrank.jskills.Player) player).getId();
    }

    private ITeam createTeam(Set<Player> players) {
        Team team = new Team();

        for (Player player : players) {
            de.gesundkrank.jskills.Player<Player> wrappedPlayer = new de.gesundkrank.jskills.Player<>(player);

            Rating rating = new Rating(
                    trueskillToMean(player.getSkill().getTrueskill(), player.getSkill().getDeviation()),
                    player.getSkill().getDeviation()
            );

            team.addPlayer(wrappedPlayer, rating);
        }

        return team;
    }

    private String teamToString(Set<Player> players) {
        return "[" + players.stream()
                .map(Objects::toString)
                .collect(Collectors.joining(", ")) + "]";
    }

    private double trueskillToMean(double trueskill, double deviation) {
        return trueskill - trueskillDeviationsFromMean * deviation;
    }

    private double meanToTrueskill(double mean, double deviation) {
        return mean + trueskillDeviationsFromMean * deviation;
    }
}
