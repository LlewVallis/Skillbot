package org.astropeci.skillbot.discord.command.player;

import org.astropeci.skillbot.data.player.Player;
import org.astropeci.skillbot.discord.Command;
import org.astropeci.skillbot.discord.CommandException;
import org.astropeci.skillbot.discord.CommandUtil;
import org.astropeci.skillbot.discord.api.Message;
import org.astropeci.skillbot.request.PagedResponse;
import org.astropeci.skillbot.request.player.ListPlayersRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.astropeci.skillbot.request.player.ListPlayersRequest.SortCriterion;

/**
 * List all players, allows for custom sorting.
 */
@Component
public class ListPlayersCommand implements Command {

    @Autowired
    private ListPlayersRequest request;

    @Override
    public String label() {
        return "players";
    }

    @Override
    public String usage() {
        return "players [page [criteria asc|desc]]";
    }

    @Override
    public String helpDescription() {
        return "list players, possibly sorted by a criterion (valid criteria: trueskill)";
    }

    @Override
    public String execute(List<String> arguments, Message message) {
        if (arguments.size() != 0 && arguments.size() != 1 && arguments.size() != 3) {
            CommandUtil.throwWrongNumberOfArguments();
        }

        SortCriterion criterion = SortCriterion.UNSPECIFIED;
        Sort.Direction direction = null;

        int page = 0;
        if (arguments.size() >= 1) {
            page = CommandUtil.pageArgument(arguments.get(0));
        }

        if (arguments.size() >= 3) {
            switch (arguments.get(1)) {
                case "trueskill":
                    criterion = SortCriterion.TRUESKILL;
                    break;
                default:
                    throw new CommandException("invalid sort criterion");
            }

            switch (arguments.get(2)) {
                case "asc":
                    direction = Sort.Direction.ASC;
                    break;
                case "desc":
                    direction = Sort.Direction.DESC;
                    break;
                default:
                    throw new CommandException("invalid sort direction");
            }
        }

        PagedResponse<Player> players = request.run(page, criterion, direction);

        if (players.getTotalElements() == 0) {
            return "No players yet";
        } else if (players.getElements().size() == 0) {
            throw new CommandException("No players on this page").setFormattedAsError(false);
        } else {
            String topLine = String.format("Showing %s-%s of %s players (page %s of %s)",
                    players.getStartingCount() + 1,
                    players.getEndingCount(),
                    players.getTotalElements(),
                    players.getPageNumber() + 1,
                    players.getTotalPages()
            );

            StringBuilder response = new StringBuilder(topLine);
            response.append("```\n");

            for (Player player : players.getElements()) {
                response.append(String.format("* %s\n", player));
            }

            response.append("```");
            return response.toString();
        }
    }
}
