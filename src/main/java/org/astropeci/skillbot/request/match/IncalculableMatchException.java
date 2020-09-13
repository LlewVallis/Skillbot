package org.astropeci.skillbot.request.match;

/**
 * Thrown when an internal error occurs calculating a match's effect, or when a nonsensical match is calculated.
 */
public class IncalculableMatchException extends RuntimeException {

    public IncalculableMatchException() {
        super();
    }

    public IncalculableMatchException(String message) {
        super(message);
    }

    public IncalculableMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncalculableMatchException(Throwable cause) {
        super(cause);
    }
}
