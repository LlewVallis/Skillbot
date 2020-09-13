package org.astropeci.skillbot.discord.command.discord;

import lombok.extern.slf4j.Slf4j;
import org.astropeci.skillbot.discord.Command;
import org.astropeci.skillbot.discord.CommandUtil;
import org.astropeci.skillbot.discord.api.Message;
import org.astropeci.skillbot.request.discord.GatewayPingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Displays the Discord gateway ping.
 */
@Slf4j
@Component
public class PingCommand implements Command {

    @Autowired
    private GatewayPingRequest request;

    @Override
    public String label() {
        return "ping";
    }

    @Override
    public String usage() {
        return "ping";
    }

    @Override
    public String helpDescription() {
        return "display the gateway ping for the bot";
    }

    @Override
    public String execute(List<String> arguments, Message message) {
        if (arguments.size() != 0) {
            CommandUtil.throwWrongNumberOfArguments();
        }

        long ping = request.run();
        return "Ping to gateway is %sms".formatted(ping);
    }
}
