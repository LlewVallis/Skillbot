package org.astropeci.skillbot.request.player;

import lombok.Getter;

/**
 * Thrown when a player was referenced by ID, but it did not exist.
 */
public class NoSuchPlayerException extends PlayerException {

    private static final String DEFAULT_MESSAGE = "no player exists with ID %s";

    @Getter
    private final long id;

    public NoSuchPlayerException(long id) {
        super(String.format(DEFAULT_MESSAGE, id));
        this.id = id;
    }

    public NoSuchPlayerException(long id, String message) {
        super(message);
        this.id = id;
    }

    public NoSuchPlayerException(long id, String message, Throwable cause) {
        super(message, cause);
        this.id = id;
    }

    public NoSuchPlayerException(long id, Throwable cause) {
        super(String.format(DEFAULT_MESSAGE, id), cause);
        this.id = id;
    }
}
