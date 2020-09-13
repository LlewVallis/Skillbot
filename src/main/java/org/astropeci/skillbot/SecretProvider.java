package org.astropeci.skillbot;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
