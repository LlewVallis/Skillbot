package org.astropeci.skillbot.request.player;

import lombok.Getter;
import org.astropeci.skillbot.data.player.Player;

/**
 * Thrown when a player is requested to be deleted but has played a match.
 */
public class PlayerHasPlayedException extends PlayerException {

    private static final String DEFAULT_MESSAGE = "%s has played a match";

    @Getter
    private final Player player;

    public PlayerHasPlayedException(Player player) {
        super(String.format(DEFAULT_MESSAGE, player));
        this.player = player;
    }

    public PlayerHasPlayedException(Player player, String message) {
        super(message);
        this.player = player;
    }

    public PlayerHasPlayedException(Player player, String message, Throwable cause) {
        super(message, cause);
        this.player = player;
    }

    public PlayerHasPlayedException(Player player, Throwable cause) {
        super(String.format(DEFAULT_MESSAGE, player), cause);
        this.player = player;
    }
}
