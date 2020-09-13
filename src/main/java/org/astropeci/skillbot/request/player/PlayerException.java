package org.astropeci.skillbot.request.player;

/**
 * Supertype for player related exceptions.
 */
public class PlayerException extends Exception {

    public PlayerException() {
        super();
    }

    public PlayerException(String message) {
        super(message);
    }

    public PlayerException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlayerException(Throwable cause) {
        super(cause);
    }
}
