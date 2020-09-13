package org.astropeci.skillbot.discord.command.match;

import org.astropeci.skillbot.data.match.Match;
import org.astropeci.skillbot.discord.CommandUtil;
import org.astropeci.skillbot.discord.api.ConfirmationFlow;
import org.astropeci.skillbot.discord.api.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SingleMatchCommand extends ProcessMatchCommandBase {

    @Autowired
    private ConfirmationFlow confirmationFlow;

    @Override
    public String label() {
        return "match";
    }

    @Override
    public String usage() {
        return "match [*]<player>{,player}[*] [*]<player>{,player}[*]";
    }

    @Override
    public String helpDescription() {
        return "Processes a match. The team surrounded with asterisks is the winner, if both teams have asterisks it " +
                "is calculated as a tie";
    }

    // Shouldn't need @Transactional despite interacting with a repo
    @Override
    public String execute(List<String> arguments, Message message) {
        if (arguments.size() != 2) {
            CommandUtil.throwWrongNumberOfArguments();
        }

        Match match = run(arguments.get(0), arguments.get(1));
        return createMatchSummary(match);
    }
}
