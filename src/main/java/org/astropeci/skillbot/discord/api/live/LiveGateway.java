package org.astropeci.skillbot.discord.api.live;

import net.dv8tion.jda.api.JDA;
import org.astropeci.skillbot.discord.api.Gateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("discord")
public class LiveGateway implements Gateway {

    @Autowired
    private JDA jda;

    @Override
    public long getPing() {
        return jda.getGatewayPing();
    }
}
