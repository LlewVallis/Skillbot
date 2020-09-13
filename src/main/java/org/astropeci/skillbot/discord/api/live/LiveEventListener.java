package org.astropeci.skillbot.discord.api.live;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.astropeci.skillbot.discord.api.CommandEvent;
import org.astropeci.skillbot.discord.api.Message;
import org.astropeci.skillbot.discord.api.ReactionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@Profile("discord")
public class LiveEventListener extends ListenerAdapter {

    @Value("${discord.bot.prefix}")
    private String prefix;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        if (!event.getChannel().getName().equals("skillbot")) {
            log.info("Ignoring bot command in channel " + event.getChannel().getName());
            return;
        }

        net.dv8tion.jda.api.entities.Message message = event.getMessage();

        if (message.getType() == MessageType.DEFAULT) {
            String content = message.getContentDisplay();

            if (content.startsWith(prefix)) {
                Message wrappedMessage = new LiveMessage(message);
                CommandEvent commandEvent = new CommandEvent(this, wrappedMessage);
                eventPublisher.publishEvent(commandEvent);
            }
        }
    }

    @Override
    @SneakyThrows({ InterruptedException.class })
    public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {
        MessageReaction.ReactionEmote emote = event.getReactionEmote();

        if (emote.isEmoji()) {
            Message message;

            try {
                message = event.getChannel().retrieveMessageById(event.getMessageId()).submit()
                        .thenApply(LiveMessage::new)
                        .get();
            } catch (ExecutionException e) {
                log.error("failed to fetch message for reaction {}, ignoring", emote, e);
                return;
            }

            ReactionEvent reactionEvent = new ReactionEvent(this, emote.getEmoji(), message);
            eventPublisher.publishEvent(reactionEvent);
        }
    }
}
