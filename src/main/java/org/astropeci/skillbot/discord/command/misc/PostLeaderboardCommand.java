package org.astropeci.skillbot.discord.command.misc;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.astropeci.skillbot.data.player.Player;
import org.astropeci.skillbot.discord.Command;
import org.astropeci.skillbot.discord.CommandException;
import org.astropeci.skillbot.discord.CommandUtil;
import org.astropeci.skillbot.discord.api.Message;
import org.astropeci.skillbot.request.PagedResponse;
import org.astropeci.skillbot.request.player.ListPlayersRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Profile("discord")
public class PostLeaderboardCommand implements Command {

    private static final int MAX_PAGES = 50;

    @Autowired
    private ListPlayersRequest request;
    @Autowired
    private JDA jda;

    @Override
    public String label() {
        return "post-leaderboard";
    }

    @Override
    public String usage() {
        return "post-leaderboard";
    }

    @Override
    public String helpDescription() {
        return "send a leaderboard message in the appropriate channel";
    }

    @Override
    public String execute(List<String> arguments, Message message) {
        if (arguments.size() != 0) {
            CommandUtil.throwWrongNumberOfArguments();
        }

        Optional<TextChannel> channelOptional = jda.getTextChannels().stream()
                .filter(channel -> channel.getName().equals("leaderboard"))
                .findAny();

        if (channelOptional.isEmpty()) {
            throw new CommandException("no leaderboard channel exists");
        }

        ListPlayersRequest.SortCriterion criterion = ListPlayersRequest.SortCriterion.TRUESKILL;
        Sort.Direction direction = Sort.Direction.DESC;

        for (int i = 0; i < MAX_PAGES; i++) {
            PagedResponse<Player> players = request.run(i, criterion, direction);

            if (players.getTotalElements() == 0) {
                throw new CommandException("no players to add to leaderboard");
            }

            if (players.getElements().size() == 0) {
                break;
            }

            StringBuilder response = new StringBuilder("```");
            for (Player player : players.getElements()) {
                String line = String.format("%s - %s", player.getDisplayName(), player.getSkill());
                response.append(line).append("\n");
            }
            response.append("\n```");

            channelOptional.get().sendMessage(response).submit().join();
        }

        return "Posted leaderboard";
    }
}
