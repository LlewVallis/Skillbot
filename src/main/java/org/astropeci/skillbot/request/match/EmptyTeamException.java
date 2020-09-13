package org.astropeci.skillbot.request.match;

/**
 * Thrown when a valid team is expected, but it was empty.
 */
public class EmptyTeamException extends MatchException {

    private static final String DEFAULT_MESSAGE = "cannot have an empty team";

    public EmptyTeamException() {
        super(DEFAULT_MESSAGE);
    }

    public EmptyTeamException(String message) {
        super(message);
    }

    public EmptyTeamException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyTeamException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
