package org.astropeci.skillbot.discord.api;

import org.astropeci.skillbot.Task;

/**
 * A message on Discord, may be responded and reacted to.
 */
public interface Message {

    String getContent();

    Task<Void> startTyping();

    Task<Message> respond(String message);

    Task<Void> react(String emote);

    Task<Void> removeReaction(String emote);

    Task<Void> clearReactions();
}
