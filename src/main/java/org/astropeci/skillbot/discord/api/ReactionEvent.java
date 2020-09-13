package org.astropeci.skillbot.discord.api;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Occurs whenever a message is reacted to with a Unicode emote (not triggered for custom emotes).
 */
public class ReactionEvent extends ApplicationEvent {

    @Getter
    private final String emote;
    @Getter
    private final Message message;

    public ReactionEvent(Object source, String emote, Message message) {
        super(source);
        this.emote = emote;
        this.message = message;
    }
}
