package org.astropeci.skillbot;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;

/**
 * Provides secret values, such as the Discord bot token.
 */
@Component
public class SecretProvider {

    private Dotenv env = Dotenv.load();

    public String getDiscordBotToken() {
        return getEnv("DISCORD_BOT_TOKEN");
    }

    private String getEnv(String variableName) {
        String value = env.get(variableName);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing environment variable " + variableName);
        }

        return value;
    }
}
