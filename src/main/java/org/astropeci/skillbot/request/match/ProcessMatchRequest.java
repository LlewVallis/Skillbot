package org.astropeci.skillbot.request.match;

import lombok.extern.slf4j.Slf4j;
import org.astropeci.skillbot.data.match.*;
import org.astropeci.skillbot.data.player.Player;
import org.astropeci.skillbot.data.player.PlayerRepository;
import org.astropeci.skillbot.data.player.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Request for a match to be calculated and the result propagated.
 *
 * This includes determining the effect of the match, storing it, and updating any relevant statistics.
 */
@Slf4j
@Component
@Validated
public class ProcessMatchRequest {

    @Autowired
    private TrueskillAdjuster calculator;

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private MatchParticipantRepository matchParticipantRepository;

    /**
     * Participant tracking its before match statistics, can be completed into a full {@link MatchParticipant}.
     */
    private static class PartialParticipant {

        private final Player player;
        private final Match match;
        private final Team team;

        private final Skill skillBefore;

        public PartialParticipant(Player player, Match match, Team team) {
            this.player = player;
            this.match = match;
            this.team = team;
            skillBefore = player.getSkill().clone();
        }

        public MatchParticipant finish() {
            return new MatchParticipant(
                    player,
                    match,
                    skillBefore,
                    player.getSkill().clone(),
                    team
            );
        }
    }

    @Transactional
    public Match run(
            @Valid Set<Player> teamA,
            @Valid Set<Player> teamB,
            MatchOutcome outcome,
            Date time
    ) throws UnbalancedTeamsException, DuplicatePlayerException, EmptyTeamException {
        if (teamA.size() == 0 || teamB.size() == 0) {
            throw new EmptyTeamException();
        }

        Match match = new Match(outcome, time);
        match = matchRepository.save(match);

        List<PartialParticipant> partialParticipants = new ArrayList<>();
        partialParticipants.addAll(createParticipants(teamA, match, Team.TEAM_A));
        partialParticipants.addAll(createParticipants(teamB, match, Team.TEAM_B));

        calculator.adjustTrueskill(teamA, teamB, outcome);

        playerRepository.saveAll(teamA);
        playerRepository.saveAll(teamB);

        List<MatchParticipant> finishedParticipants = partialParticipants.stream()
                .map(PartialParticipant::finish)
                .collect(Collectors.toList());

        matchParticipantRepository.saveAll(finishedParticipants);
        match.getParticipants().addAll(finishedParticipants);

        log.info("Calculated match between %s and %s. Outcome was %s".formatted(
                teamA.stream().map(Object::toString).collect(Collectors.joining(", ")),
                teamB.stream().map(Object::toString).collect(Collectors.joining(", ")),
                outcome
        ));

        return match;
    }

    private Set<PartialParticipant> createParticipants(Set<Player> players, Match match, Team team) {
        return players.stream()
                .map(player -> new PartialParticipant(player, match, team))
                .collect(Collectors.toSet());
    }
}
