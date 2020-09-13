package org.astropeci.skillbot.discord.api.live;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.astropeci.skillbot.SecretProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.security.auth.login.LoginException;
import java.util.List;

/**
 * Constructs and configures the JDA instance for Discord integration.
 */
@Slf4j
@Configuration
@Profile("discord")
public class LiveProvider {

    @Value("${discord.bot.status}")
    private String status;

    @Autowired
    private EventListener eventDispatcher;
    @Autowired
    private SecretProvider secretProvider;

    @Bean
    public JDA jda() throws LoginException {
        String botToken = secretProvider.getDiscordBotToken();

        return JDABuilder.create(botToken, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS)
                .setActivity(Activity.playing(status))
                .addEventListeners(eventDispatcher)

                // We don't have access to relevant intents so can't use cache. Should be turned back on as more intents
                // are used for something
                .setMemberCachePolicy(MemberCachePolicy.NONE)
                .disableCache(List.of(CacheFlag.values()))

                .build();
    }
}
