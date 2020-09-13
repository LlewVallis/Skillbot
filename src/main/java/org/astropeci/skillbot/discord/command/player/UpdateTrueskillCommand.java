package org.astropeci.skillbot.discord.command.player;

import lombok.SneakyThrows;
import org.astropeci.skillbot.data.player.Player;
import org.astropeci.skillbot.data.player.PlayerRepository;
import org.astropeci.skillbot.discord.Command;
import org.astropeci.skillbot.discord.CommandUtil;
import org.astropeci.skillbot.discord.api.Message;
import org.astropeci.skillbot.request.IdNotAssignedException;
import org.astropeci.skillbot.request.player.NoSuchPlayerException;
import org.astropeci.skillbot.request.player.UpdatePlayerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
public class UpdateTrueskillCommand implements Command {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private UpdatePlayerRequest request;

    private final PlayerResolver playerResolver = new PlayerResolver();

    @Override
    public String label() {
        return "update-trueskill";
    }

    @Override
    public String usage() {
        return "update-trueskill <player> <new-trueskill> <new-deviation>";
    }

    @Override
    public String helpDescription() {
        return "update the trueskill values for a player";
    }

    @Override
    @Transactional
    @SneakyThrows({ IdNotAssignedException.class, NoSuchPlayerException.class })
    public String execute(List<String> arguments, Message message) {
        if (arguments.size() != 3) {
            CommandUtil.throwWrongNumberOfArguments();
        }

        Player player = playerResolver.resolve(arguments.get(0), playerRepository);

        double trueskill = CommandUtil.doubleArgument(arguments.get(1), "trueskill");
        double deviation = CommandUtil.doubleArgument(arguments.get(2), "deviation");

        player.getSkill().setTrueskill(trueskill);
        player.getSkill().setDeviation(deviation);

        player = request.run(player);

        return "Trueskill updated for " + player.getDisplayName();
    }
}
