package org.astropeci.skillbot.request.match;

import lombok.Getter;

/**
 * Thrown when the amount of matches requested for reversion was unallowable and would cause performance issues.
 */
public class RevertTooLargeException extends MatchException {

    private static final String DEFAULT_MESSAGE = "you can revert at most %s matches";

    /**
     * The maximum amount of matches that can be undone in a single request.
     */
    @Getter
    private final int maximum;

    public RevertTooLargeException(int maximum) {
        super(DEFAULT_MESSAGE.formatted(maximum));
        this.maximum = maximum;
    }

    public RevertTooLargeException(int maximum, String message) {
        super(message);
        this.maximum = maximum;
    }

    public RevertTooLargeException(int maximum, String message, Throwable cause) {
        super(message, cause);
        this.maximum = maximum;
    }

    public RevertTooLargeException(int maximum, Throwable cause) {
        super(DEFAULT_MESSAGE.formatted(maximum), cause);
        this.maximum = maximum;
    }
}
