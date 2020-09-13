package org.astropeci.skillbot.discord.api.noop;

import org.astropeci.skillbot.discord.api.Gateway;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!discord")
public class NoopGateway implements Gateway {

    @Override
    public long getPing() {
        return 0;
    }
}
