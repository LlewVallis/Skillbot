package org.astropeci.skillbot.request;

import org.astropeci.skillbot.request.player.PlayerException;

/**
 * Thrown when an entity was expected to have an ID, yet it did not.
 */
public class IdNotAssignedException extends PlayerException {

    private static final String DEFAULT_MESSAGE = "expected entity to be assigned an ID, but it was missing";

    public IdNotAssignedException() {
        super(DEFAULT_MESSAGE);
    }

    public IdNotAssignedException(String message) {
        super(message);
    }

    public IdNotAssignedException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdNotAssignedException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
