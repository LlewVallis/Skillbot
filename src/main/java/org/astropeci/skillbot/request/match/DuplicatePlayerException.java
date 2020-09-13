package org.astropeci.skillbot.request.match;

import lombok.Getter;
import org.astropeci.skillbot.data.player.Player;

/**
 * Thrown when a player exists twice over a set of teams.
 */
public class DuplicatePlayerException extends MatchException {

    private static final String DEFAULT_MESSAGE = "%s appeared more than once in the same match";

    @Getter
    private final Player player;

    public DuplicatePlayerException(Player player) {
        super(DEFAULT_MESSAGE.formatted(player));
        this.player = player;
    }

    public DuplicatePlayerException(Player player, String message) {
        super(message);
        this.player = player;
    }

    public DuplicatePlayerException(Player player, String message, Throwable cause) {
        super(message, cause);
        this.player = player;
    }

    public DuplicatePlayerException(Player player, Throwable cause) {
        super(DEFAULT_MESSAGE.formatted(player), cause);
        this.player = player;
    }
}
