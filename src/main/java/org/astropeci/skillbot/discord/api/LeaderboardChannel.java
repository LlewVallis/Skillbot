package org.astropeci.skillbot.discord.api;

import org.astropeci.skillbot.Task;

public interface LeaderboardChannel {

    Task<Void> clear();

    Task<Void> post(String message);
}
