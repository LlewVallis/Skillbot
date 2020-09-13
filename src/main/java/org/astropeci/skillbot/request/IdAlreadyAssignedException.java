package org.astropeci.skillbot.request;

import lombok.Getter;

/**
 * Thrown when a new entity is being created, but the ID for said entity already has a value.
 */
public class IdAlreadyAssignedException extends Exception {

    private static final String DEFAULT_MESSAGE = "the entity's ID %s should be blank";

    @Getter
    private final long id;

    public IdAlreadyAssignedException(long id) {
        super(String.format(DEFAULT_MESSAGE, id));
        this.id = id;
    }

    public IdAlreadyAssignedException(long id, String message) {
        super(message);
        this.id = id;
    }

    public IdAlreadyAssignedException(long id, String message, Throwable cause) {
        super(message, cause);
        this.id = id;
    }

    public IdAlreadyAssignedException(long id, Throwable cause) {
        super(String.format(DEFAULT_MESSAGE, id), cause);
        this.id = id;
    }
}
