package org.astropeci.skillbot.request.match;

import lombok.Getter;

/**
 * Thrown when a match referenced by ID did not exist.
 */
public class NoSuchMatchException extends Exception {

    private static final String DEFAULT_MESSAGE = "no match exists with ID %s";

    @Getter
    private final long id;

    public NoSuchMatchException(long id) {
        super(DEFAULT_MESSAGE.formatted(id));
        this.id = id;
    }

    public NoSuchMatchException(long id, String message) {
        super(message);
        this.id = id;
    }

    public NoSuchMatchException(long id, String message, Throwable cause) {
        super(message, cause);
        this.id = id;
    }

    public NoSuchMatchException(long id, Throwable cause) {
        super(DEFAULT_MESSAGE.formatted(id), cause);
        this.id = id;
    }
}
