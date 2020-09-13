package org.astropeci.skillbot.discord.command.player;

import org.astropeci.skillbot.MiscUtil;
import org.astropeci.skillbot.data.player.Player;
import org.astropeci.skillbot.data.player.PlayerRepository;
import org.astropeci.skillbot.discord.CommandException;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Maps player specifiers into {@link Player} instances.
 *
 * A player specifier is a string used in command arguments to flexibly specify a player to be used.
 */
public class PlayerResolver {

    public Player resolve(String specifier, PlayerRepository repository) {
        if (specifier.isBlank()) {
            throw new CommandException("cannot have blank player");
        }

        if (specifier.startsWith("#")) {
            return byId(specifier, repository);
        }

        if (specifier.startsWith("/")) {
            return byDiscordId(specifier, repository);
        }

        return byPartialName(specifier, repository);
    }

    private Player byId(String specifier, PlayerRepository repository) {
        try {
            long id = Long.parseLong(specifier.substring(1));
            Optional<Player> player = repository.findById(id);

            return player.orElseThrow(() -> new CommandException("no player exists with internal ID " + id));
        } catch (NumberFormatException e) {
            throw new CommandException("invalid internal ID", e);
        }
    }

    private Player byDiscordId(String specifier, PlayerRepository repository) {
        try {
            String discordId = specifier.substring(1);
            Long.parseUnsignedLong(discordId);

            Optional<Player> player = repository.findByDiscordId(discordId);

            return player.orElseThrow(() -> new CommandException("no player exists with discord ID " + discordId));
        } catch (NumberFormatException e) {
            throw new CommandException("invalid discord ID", e);
        }
    }

    private Player byPartialName(String specifier, PlayerRepository repository) {
        Set<Player> matches = repository.findByDisplayNameContainingIgnoreCase(specifier);

        // If we have multiple matches, use more restrictive prefix matching
        if (matches.size() > 1) {
            Set<Player> prefixMatches = matches.stream()
                    .filter(player -> MiscUtil.startsWithIgnoreCase(player.getDisplayName(), specifier))
                    .collect(Collectors.toSet());

            if (prefixMatches.size() != 0) {
                matches = prefixMatches;
            }
        }

        if (matches.size() == 0) {
            throw new CommandException("no players exist with '%s' in their name".formatted(specifier));
        }

        if (matches.size() > 1) {
            StringBuilder errorMessage = new StringBuilder("'%s' matched multiple players:\n".formatted(specifier));

            for (Player player : matches) {
                errorMessage.append("* %s\n".formatted(player));
            }

            throw new CommandException(errorMessage.toString());
        }

        return matches.stream().findAny().get();
    }
}
