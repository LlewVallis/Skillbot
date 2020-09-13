package org.astropeci.skillbot.request.match;

import lombok.Getter;

/**
 * Thrown when the amount of history requested for reversion was larger than the actual amount of history.
 */
public class InsufficientHistoryException extends MatchException {

    private static final String DEFAULT_MESSAGE = "not enough history, history is %s long";

    @Getter
    private final int historySize;

    public InsufficientHistoryException(int historySize) {
        super(DEFAULT_MESSAGE);
        this.historySize = historySize;
    }

    public InsufficientHistoryException(int historySize, String message) {
        super(message);
        this.historySize = historySize;
    }

    public InsufficientHistoryException(int historySize, String message, Throwable cause) {
        super(message, cause);
        this.historySize = historySize;
    }

    public InsufficientHistoryException(int historySize, Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
        this.historySize = historySize;
    }
}
