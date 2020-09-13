package org.astropeci.skillbot.discord.command.player;

import lombok.SneakyThrows;
import org.astropeci.skillbot.data.player.Player;
import org.astropeci.skillbot.data.player.PlayerRepository;
import org.astropeci.skillbot.discord.Command;
import org.astropeci.skillbot.discord.CommandException;
import org.astropeci.skillbot.discord.CommandUtil;
import org.astropeci.skillbot.discord.api.ConfirmationFlow;
import org.astropeci.skillbot.discord.api.Message;
import org.astropeci.skillbot.request.IdNotAssignedException;
import org.astropeci.skillbot.request.player.DeletePlayerRequest;
import org.astropeci.skillbot.request.player.NoSuchPlayerException;
import org.astropeci.skillbot.request.player.PlayerHasPlayedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
public class DeletePlayerCommand implements Command {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private DeletePlayerRequest request;

    @Autowired
    private ConfirmationFlow confirmationFlow;

    private final PlayerResolver playerResolver = new PlayerResolver();

    @Override
    public String label() {
        return "delete-player";
    }

    @Override
    public String usage() {
        return "delete-player <player-spec>";
    }

    @Override
    public String helpDescription() {
        return "delete a player";
    }

    @SneakyThrows({ NoSuchPlayerException.class, IdNotAssignedException.class })
    @Override
    @Transactional
    public String execute(List<String> arguments, Message message) {
        if (arguments.size() != 1) {
            CommandUtil.throwWrongNumberOfArguments();
        }

        Player player = playerResolver.resolve(arguments.get(0), playerRepository);

        String prompt = "Are you sure you want to delete %s?".formatted(player);
        boolean confirmed = confirmationFlow.askForConfirmation(message, prompt).waitFor();

        if (confirmed) {
            try {
                request.run(player);
            } catch (PlayerHasPlayedException e) {
                throw new CommandException(e.getMessage());
            }
        } else {
            throw new CommandException("Cancelled deletion of " + player).setFormattedAsError(false);
        }

        return "Deleted player " + player;
    }
}
