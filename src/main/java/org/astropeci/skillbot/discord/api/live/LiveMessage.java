package org.astropeci.skillbot.discord.api.live;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.astropeci.skillbot.Task;
import org.astropeci.skillbot.discord.api.Message;

import java.util.concurrent.CompletableFuture;

@EqualsAndHashCode
@Slf4j
@RequiredArgsConstructor
public class LiveMessage implements Message {

    private final net.dv8tion.jda.api.entities.Message message;

    @Override
    public String getContent() {
        return message.getContentDisplay();
    }

    @Override
    public Task<Void> startTyping() {
        MessageChannel channel = getChannel();

        return Task.of(channel.sendTyping().submit(), null)
                .logOnError(log, "failed to send typing in channel {}", channel);
    }

    @Override
    public Task<Message> respond(String message) {
        MessageChannel channel = getChannel();

        CompletableFuture<Message> future = channel.sendMessage(message).submit().thenApply(LiveMessage::new);

        return Task.of(future, null)
                .logOnError(log, "failed to send response to {} in channel {}", message, channel);
    }

    @Override
    public Task<Void> react(String emote) {
        return Task.of(message.addReaction(emote).submit(), null)
                .logOnError(log, "failed to add reaction {} to message {}", emote, message);
    }

    @Override
    public Task<Void> removeReaction(String emote) {
        return Task.of(message.removeReaction("🔄").submit(), null)
                .logOnError(log, "failed to remove reaction {} from message {}", emote, message);
    }

    @Override
    public Task<Void> clearReactions() {
        return Task.of(message.clearReactions().submit(), null)
                .logOnError(log, "failed to clear reactions from message {}", message);
    }

    private MessageChannel getChannel() {
        return message.getChannel();
    }

    @Override
    public String toString() {
        return message.toString();
    }
}
