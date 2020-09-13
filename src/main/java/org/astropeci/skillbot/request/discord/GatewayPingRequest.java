package org.astropeci.skillbot.request.discord;

import org.astropeci.skillbot.discord.api.Gateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Request the ping to the Discord gateway.
 *
 * This does not calculate the ping - it simply returns a cached value.
 */
@Component
public class GatewayPingRequest {

    @Autowired
    private Gateway gateway;

    public long run() {
        return gateway.getPing();
    }
}
