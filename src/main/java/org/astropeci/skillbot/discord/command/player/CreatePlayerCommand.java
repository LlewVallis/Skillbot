package org.astropeci.skillbot.discord.command.player;

import lombok.SneakyThrows;
import org.astropeci.skillbot.data.player.Player;
import org.astropeci.skillbot.data.player.Skill;
import org.astropeci.skillbot.discord.Command;
import org.astropeci.skillbot.discord.CommandException;
import org.astropeci.skillbot.discord.CommandUtil;
import org.astropeci.skillbot.discord.api.Message;
import org.astropeci.skillbot.request.IdAlreadyAssignedException;
import org.astropeci.skillbot.request.player.CreatePlayerRequest;
import org.astropeci.skillbot.request.player.DiscordIdAlreadyUsedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Create a new player, allowing for default skill values.
 */
@Component
public class CreatePlayerCommand implements Command {

    @Autowired
    private CreatePlayerRequest request;

    @Value("${trueskill.default.trueskill}")
    private double defaultTrueskill;
    @Value("${trueskill.default.deviation}")
    private double defaultDeviation;

    @Override
    public String label() {
        return "create-player";
    }

    @Override
    public String usage() {
        return "create-player <name> [trueskill deviation] <id>";
    }

    @Override
    public String helpDescription() {
        return "create a new player, possibly with non-default trueskill values";
    }

    @Override
    @SneakyThrows({ IdAlreadyAssignedException.class })
    public String execute(List<String> arguments, Message message) {
        if (arguments.size() != 2 && arguments.size() != 4) {
            CommandUtil.throwWrongNumberOfArguments();
        }

        String displayName = arguments.get(0);

        double trueskill = defaultTrueskill;
        double deviation = defaultDeviation;

        if (arguments.size() == 4) {
            trueskill = CommandUtil.doubleArgument(arguments.get(1), "trueskill");
            deviation = CommandUtil.doubleArgument(arguments.get(2), "deviation");
        }

        String discordId = CommandUtil.discordIdArgument(arguments.get(arguments.size() - 1));

        Player player = new Player(displayName, discordId, new Skill(trueskill, deviation));

        try {
            request.run(player);
        } catch (DiscordIdAlreadyUsedException e) {
            throw new CommandException(e.getMessage(), e);
        }

        return "Created player " + player.getDisplayName();
    }
}
