package org.astropeci.skillbot.request.match;

/**
 * Supertype for match related exceptions.
 */
public class MatchException extends Exception {

    public MatchException() {
        super();
    }

    public MatchException(String message) {
        super(message);
    }

    public MatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public MatchException(Throwable cause) {
        super(cause);
    }
}
