package org.astropeci.skillbot.discord.api;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class CommandEvent extends ApplicationEvent {

    @Getter
    private final Message message;

    public CommandEvent(Object source, Message message) {
        super(source);
        this.message = message;
    }
}
