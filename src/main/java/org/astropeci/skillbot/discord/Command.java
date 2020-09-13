package org.astropeci.skillbot.discord;

import org.astropeci.skillbot.discord.api.Message;

import java.util.List;

/**
 * Any bean conforming to this interface can be loaded and used as a Discord command.
 */
public interface Command {

    String label();

    String usage();

    String helpDescription();

    String execute(List<String> arguments, Message message);
}
