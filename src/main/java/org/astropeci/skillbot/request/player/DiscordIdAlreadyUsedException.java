package org.astropeci.skillbot.request.player;

import lombok.Getter;

/**
 * Thrown when a new player is being created with a used Discord ID.
 */
public class DiscordIdAlreadyUsedException extends PlayerException {

    private static final String DEFAULT_MESSAGE  = "the Discord ID %s is already in use";

    @Getter
    private final String discordId;

    public DiscordIdAlreadyUsedException(String discordId) {
        super(String.format(DEFAULT_MESSAGE, discordId));
        this.discordId = discordId;
    }

    public DiscordIdAlreadyUsedException(String discordId, String message) {
        super(message);
        this.discordId = discordId;
    }

    public DiscordIdAlreadyUsedException(String discordId, String message, Throwable cause) {
        super(message, cause);
        this.discordId = discordId;
    }

    public DiscordIdAlreadyUsedException(String discordId, Throwable cause) {
        super(String.format(DEFAULT_MESSAGE, discordId), cause);
        this.discordId = discordId;
    }
}
