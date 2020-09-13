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
public class UpdateDisplayNameCommand implements Command {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private UpdatePlayerRequest request;

    private final PlayerResolver playerResolver = new PlayerResolver();

    @Override
    public String label() {
        return "update-name";
    }

    @Override
    public String usage() {
        return "update-name <player> <new-name>";
    }

    @Override
    public String helpDescription() {
        return "update the display name for a player";
    }

    @Override
    @Transactional
    @SneakyThrows({ IdNotAssignedException.class, NoSuchPlayerException.class })
    public String execute(List<String> arguments, Message message) {
        if (arguments.size() != 2) {
            CommandUtil.throwWrongNumberOfArguments();
        }

        Player player = playerResolver.resolve(arguments.get(0), playerRepository);
        String oldName = player.getDisplayName();

        player.setDisplayName(arguments.get(1));

        player = request.run(player);

        return String.format("Renamed %s to %s", oldName, player.getDisplayName());
    }
}
