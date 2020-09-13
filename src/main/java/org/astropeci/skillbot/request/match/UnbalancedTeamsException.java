package org.astropeci.skillbot.request.match;

/**
 * Thrown when two or more teams have a differing number of players.
 */
public class UnbalancedTeamsException extends MatchException {

    private static final String DEFAULT_MESSAGE = "each team must have the same number of players";

    public UnbalancedTeamsException() {
        super(DEFAULT_MESSAGE);
    }

    public UnbalancedTeamsException(String message) {
        super(message);
    }

    public UnbalancedTeamsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnbalancedTeamsException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
