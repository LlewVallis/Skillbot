package org.astropeci.skillbot.discord;

import lombok.RequiredArgsConstructor;
import org.astropeci.skillbot.discord.api.Message;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Specialised help command for displaying information on other commands.
 */
@RequiredArgsConstructor
class HelpCommand implements Command {

    private final List<Command> commands;

    @Override
    public String label() {
        return "help";
    }

    @Override
    public String usage() {
        return "help [search]";
    }

    @Override
    public String helpDescription() {
        return "lists all commands and their purpose";
    }

    @Override
    public String execute(List<String> arguments, Message message) {
        if (arguments.size() != 0 && arguments.size() != 1) {
            CommandUtil.throwWrongNumberOfArguments();
        }

        final String search;
        if (arguments.size() >= 1) {
            search = arguments.get(0);
        } else {
            search = "";
        }

        Set<Command> relevantCommands = commands.stream()
                .filter(command -> containsIgnoreCase(command.label(), search))
                .collect(Collectors.toSet());

        if (relevantCommands.size() == 0) {
            throw new CommandException("No commands found matching that search").setFormattedAsError(false);
        }

        StringBuilder response = new StringBuilder("```\n");

        for (Command command : relevantCommands) {
            String line = String.format("* %s\n%s\n\n", command.usage(), command.helpDescription());
            response.append(line);
        }

        response.append("```");

        return response.toString();
    }

    private boolean containsIgnoreCase(String haystack, String needle) {
        Pattern regex = Pattern.compile(Pattern.quote(needle), Pattern.CASE_INSENSITIVE);
        return regex.matcher(haystack).find();
    }
}
