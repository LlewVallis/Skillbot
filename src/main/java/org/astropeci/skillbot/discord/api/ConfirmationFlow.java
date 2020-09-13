package org.astropeci.skillbot.discord.api;

import lombok.RequiredArgsConstructor;
import org.astropeci.skillbot.Task;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Allows the creation of confirmation messages in Discord.
 */
@Component
@RequiredArgsConstructor
public class ConfirmationFlow implements ApplicationListener<ReactionEvent> {

    @Value("${discord.bot.emote.confirm}")
    private String confirmEmote;
    @Value("${discord.bot.confirm-timeout}")
    private int confirmTimeout;

    private Map<Message, CompletableFuture<Boolean>> awaitingMessages = new ConcurrentHashMap<>();
    private Object awaitingMessagesLock = new Object();

    /**
     * Sends a confirmation message in the same channel as the source.
     *
     * This message must be reacted to with the confirmation emote, otherwise a default of false will be returned after
     * the confirm timeout has passed.
     */
    public Task<Boolean> askForConfirmation(Message sourceMessage, String prompt) {
        String fullPrompt = createFullPrompt(prompt);

        CompletableFuture<Boolean> future = new CompletableFuture<Boolean>()
                .whenComplete((value, e) -> removeAwaitingMessage(sourceMessage))
                .completeOnTimeout(false, 10, TimeUnit.SECONDS);

        Message confirmationMessage = sourceMessage.respond(fullPrompt).waitFor();
        addAwaitingMessage(confirmationMessage, future);

        return Task.of(future, false);
    }

    private String createFullPrompt(String prompt) {
        return String.format("%s\nReact with %s to confirm", prompt, confirmEmote);
    }

    private void addAwaitingMessage(Message message, CompletableFuture<Boolean> future) {
        awaitingMessages.put(message, future);
    }

    private void removeAwaitingMessage(Message message) {
        awaitingMessages.remove(message);
    }

    private void possiblyConfirm(Message message) {
        CompletableFuture<Boolean> future = awaitingMessages.get(message);

        if (future != null) {
            future.complete(true);
        }
    }

    @Override
    public void onApplicationEvent(ReactionEvent event) {
        if (event.getEmote().equals(confirmEmote)) {
            possiblyConfirm(event.getMessage());
        } else if (awaitingMessages.containsKey(event.getMessage())) {
            event.getMessage().clearReactions();
        }
    }
}
