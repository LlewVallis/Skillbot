package org.astropeci.skillbot.discord.command.match;

import org.astropeci.skillbot.data.match.Match;
import org.astropeci.skillbot.discord.Command;
import org.astropeci.skillbot.discord.CommandException;
import org.astropeci.skillbot.discord.CommandUtil;
import org.astropeci.skillbot.discord.api.Message;
import org.astropeci.skillbot.request.match.InsufficientHistoryException;
import org.astropeci.skillbot.request.match.RevertMatchesRequest;
import org.astropeci.skillbot.request.match.RevertTooLargeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RevertMatchesCommand implements Command {

    @Autowired
    private RevertMatchesRequest request;

    @Override
    public String label() {
        return "undo-match";
    }

    @Override
    public String usage() {
        return "undo-match [count]";
    }

    @Override
    public String helpDescription() {
        return "reverts number of calculated past matches";
    }

    @Override
    public String execute(List<String> arguments, Message message) {
        if (arguments.size() != 0 && arguments.size() != 1) {
            CommandUtil.throwWrongNumberOfArguments();
        }

        int count = 1;
        if (arguments.size() == 1) {
            count = CommandUtil.unsignedIntArgument(arguments.get(0), "count");
        }

        if (count == 0) {
            return "Successfully did nothing";
        }

        List<Match> matches;
        try {
            matches = request.run(count);
        } catch (InsufficientHistoryException e) {
            String errorMessage = String.format("There are only %s matches to undo", e.getHistorySize());
            throw new CommandException(errorMessage, e).setFormattedAsError(false);
        } catch (RevertTooLargeException e) {
            throw new CommandException(e.getMessage(), e);
        }

        String header = String.format("Reverted %s matches", matches.size());

        StringBuilder response = new StringBuilder(header);
        response.append("```\n");

        for (Match match : matches) {
            response.append(String.format("* %s\n", match));
        }

        response.append("```");
        return response.toString();
    }
}
