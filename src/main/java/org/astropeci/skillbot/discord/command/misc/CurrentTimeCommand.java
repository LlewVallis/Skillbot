package org.astropeci.skillbot.discord.command.misc;

import org.astropeci.skillbot.MiscUtil;
import org.astropeci.skillbot.discord.Command;
import org.astropeci.skillbot.discord.CommandUtil;
import org.astropeci.skillbot.discord.api.Message;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class CurrentTimeCommand implements Command {

    @Override
    public String label() {
        return "current-time";
    }

    @Override
    public String usage() {
        return "current-time";
    }

    @Override
    public String helpDescription() {
        return "display the current time in UTC";
    }

    @Override
    public String execute(List<String> arguments, Message message) {
        if (arguments.size() != 0) {
            CommandUtil.throwWrongNumberOfArguments();
        }

        SimpleDateFormat dateFormat = MiscUtil.getDateFormat();
        return "Current time UTC is " + dateFormat.format(new Date());
    }
}
