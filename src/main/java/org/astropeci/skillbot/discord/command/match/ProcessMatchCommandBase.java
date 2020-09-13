package org.astropeci.skillbot.discord.command.match;

import lombok.Value;
import org.astropeci.skillbot.data.match.Match;
import org.astropeci.skillbot.data.match.MatchOutcome;
import org.astropeci.skillbot.data.match.MatchParticipant;
import org.astropeci.skillbot.data.player.Player;
import org.astropeci.skillbot.data.player.PlayerRepository;
import org.astropeci.skillbot.discord.Command;
import org.astropeci.skillbot.discord.CommandException;
import org.astropeci.skillbot.discord.command.player.PlayerResolver;
import org.astropeci.skillbot.request.match.DuplicatePlayerException;
import org.astropeci.skillbot.request.match.EmptyTeamException;
import org.astropeci.skillbot.request.match.ProcessMatchRequest;
import org.astropeci.skillbot.request.match.UnbalancedTeamsException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Calculates a match between two teams.
 */
public abstract class ProcessMatchCommandBase implements Command {

    @Autowired
    private ProcessMatchRequest processMatchRequest;

    @Autowired
    private PlayerRepository playerRepository;

    private final PlayerResolver resolver = new PlayerResolver();

    /**
     * Container for the data extracted out a team argument.
     */
    @Value
    private static class ParsedTeam {
        Set<Player> players;
        boolean winner;
    }

    @Transactional
    protected Match run(String team1Argument, String team2Argument) {
        ParsedTeam teamA = parseTeam(team1Argument, playerRepository);
        ParsedTeam teamB = parseTeam(team2Argument, playerRepository);

        if (teamB.isWinner()) {
            ParsedTeam swap = teamA;
            teamA = teamB;
            teamB = swap;
        }

        if (teamA.isWinner()) {
            MatchOutcome outcome = teamB.isWinner() ? MatchOutcome.TIE : MatchOutcome.TEAM_A_WINS;

            try {
                return processMatchRequest.run(teamA.getPlayers(), teamB.getPlayers(), outcome, new Date());
            } catch (UnbalancedTeamsException | DuplicatePlayerException | EmptyTeamException e) {
                throw new CommandException(e.getMessage(), e);
            }
        } else {
            throw new CommandException("must have either one or two winners");
        }
    }

    /**
     * Creates the body of the response, doing work otherwise.
     */
    protected String createMatchSummary(Match match) {
        Set<MatchParticipant> teamA = match.getTeamA();
        Set<MatchParticipant> teamB = match.getTeamB();

        String header = String.format("Calculated %s", match);

        StringBuilder builder = new StringBuilder(header);
        builder.append("```\n");

        List<MatchParticipant> bothTeams = Stream.concat(teamA.stream(), teamB.stream()).collect(Collectors.toList());
        for (MatchParticipant participant : bothTeams) {
            builder.append(String.format("* %s\n", participant));
        }

        builder.append("```");

        return builder.toString();
    }

    /**
     * Parses a team argument, possibly surrounded by asterisks to indicate a winner.
     */
    private ParsedTeam parseTeam(String team, PlayerRepository playerRepository) {
        boolean winner = false;
        if (team.matches("\\*.*\\*")) {
            winner = true;
            team = team.substring(1, team.length() - 1);
        }

        Set<Player> participants = new HashSet<>();
        String[] playerSpecifiers = team.split(",");

        for (String specifier : playerSpecifiers) {
            Player player = resolver.resolve(specifier, playerRepository);

            if (!participants.add(player)) {
                DuplicatePlayerException duplicateException = new DuplicatePlayerException(player);
                throw new CommandException(duplicateException.getMessage(), duplicateException);
            }
        }

        return new ParsedTeam(participants, winner);
    }
}
