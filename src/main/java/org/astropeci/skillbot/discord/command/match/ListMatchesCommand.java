package org.astropeci.skillbot.discord.command.match;

import org.astropeci.skillbot.data.match.Match;
import org.astropeci.skillbot.discord.Command;
import org.astropeci.skillbot.discord.CommandException;
import org.astropeci.skillbot.discord.CommandUtil;
import org.astropeci.skillbot.discord.api.Message;
import org.astropeci.skillbot.request.PagedResponse;
import org.astropeci.skillbot.request.match.ListMatchesRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListMatchesCommand implements Command {

    @Autowired
    private ListMatchesRequest request;

    @Override
    public String label() {
        return "history";
    }

    @Override
    public String usage() {
        return "history [page]";
    }

    @Override
    public String helpDescription() {
        return "list matches, starting from the most recent";
    }

    @Override
    public String execute(List<String> arguments, Message message) {
        if (arguments.size() != 0 && arguments.size() != 1) {
            CommandUtil.throwWrongNumberOfArguments();
        }

        int page = 0;
        if (arguments.size() >= 1) {
            page = CommandUtil.pageArgument(arguments.get(0));
        }

        PagedResponse<Match> matches = request.run(page);

        if (matches.getTotalElements() == 0) {
            return "No matches yet";
        } else if (matches.getElements().size() == 0) {
            throw new CommandException("No matches on this page").setFormattedAsError(false);
        } else {
            String topLine = "Showing %s-%s of %s matches (page %s of %s)".formatted(
                    matches.getStartingCount() + 1,
                    matches.getEndingCount(),
                    matches.getTotalElements(),
                    matches.getPageNumber() + 1,
                    matches.getTotalPages()
            );

            StringBuilder response = new StringBuilder(topLine);
            response.append("```\n");

            for (Match match : matches.getElements()) {
                response.append("* %s\n".formatted(match));
            }

            response.append("```");
            return response.toString();
        }
    }
}
