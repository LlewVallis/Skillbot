package org.astropeci.skillbot.discord;

import lombok.Getter;

/**
 * A generic user or otherwise expected error that occurred when processing a command.
 *
 * The message of the error is displayed to the user in a code block.
 * {@link CommandException#setFormattedAsError(boolean)} can be used to configure the error to be displayed as a normal
 * message.
 */
public class CommandException extends RuntimeException {

    @Getter
    private boolean formattedAsError = true;

    public CommandException(String message) {
        super(message);
    }

    public CommandException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandException setFormattedAsError(boolean value) {
        formattedAsError = value;
        return this;
    }
}
