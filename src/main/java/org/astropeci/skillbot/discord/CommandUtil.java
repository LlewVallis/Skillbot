package org.astropeci.skillbot.discord;

import lombok.experimental.UtilityClass;

/**
 * Various common utilities for working in the command layer.
 */
@UtilityClass
public class CommandUtil {

    /**
     * Throws a {@link CommandException} explaining that the wrong number of arguments were passed to the command.
     *
     * @return nothing; always throws
     */
    public <T> T throwWrongNumberOfArguments() {
        throw new CommandException("wrong number of arguments");
    }

    /**
     * Parses an argument as a long. Throws a {@link CommandException} on invalid data.
     */
    public String discordIdArgument(String argument) {
        try {
            Long.parseUnsignedLong(argument);
            return argument;
        } catch (NumberFormatException e) {
            throw new CommandException("malformed Discord ID", e);
        }
    }

    /**
     * Parses an argument as an unsigned integer. Throws a {@link CommandException} on invalid data.
     *
     * @param argumentDescription a description of what is being passed for the error message
     */
    public int unsignedIntArgument(String argument, String argumentDescription) {
        try {
            int value = Integer.parseInt(argument);

            if (value < 0) {
                throw new CommandException(argumentDescription + " cannot be negative");
            }

            return value;
        } catch (NumberFormatException e) {
            throw new CommandException("malformed " + argumentDescription, e);
        }
    }

    /**
     * Parses an argument as an unsigned long. Throws a {@link CommandException} on invalid data.
     *
     * @param argumentDescription a description of what is being passed for the error message
     */
    public long unsignedLongArgument(String argument, String argumentDescription) {
        try {
            long value = Long.parseLong(argument);

            if (value < 0) {
                throw new CommandException(argumentDescription + " cannot be negative");
            }

            return value;
        } catch (NumberFormatException e) {
            throw new CommandException("malformed " + argumentDescription, e);
        }
    }

    /**
     * Parses an argument as a 1-indexed page number. Throws a {@link CommandException} on invalid data.
     */
    public int pageArgument(String argument) {
        try {
            int value = Integer.parseInt(argument) - 1;

            if (value == -1) {
                throw new CommandException("page numbers start at one");
            } else if (value < 0) {
                throw new CommandException("page cannot be negative");
            }

            return value;
        } catch (NumberFormatException e) {
            throw new CommandException("malformed page", e);
        }
    }

    /**
     * Parses an argument as a double. Throws a {@link CommandException} on invalid data.
     *
     * @param argumentDescription a description of what is being passed for the error message
     */public double doubleArgument(String argument, String argumentDescription) {
        try {
            return Double.parseDouble(argument);
        } catch (NumberFormatException e) {
            throw new CommandException("malformed " + argumentDescription, e);
        }
    }
}
