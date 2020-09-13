package org.astropeci.skillbot.request.player;

import lombok.extern.slf4j.Slf4j;
import org.astropeci.skillbot.data.player.Player;
import org.astropeci.skillbot.data.player.PlayerRepository;
import org.astropeci.skillbot.request.IdAlreadyAssignedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * Create a new player, does not assume any defaults.
 */
@Slf4j
@Validated
@Component
public class CreatePlayerRequest {

    @Autowired
    private PlayerRepository playerRepository;

    @Transactional
    public Player run(@Valid Player player) throws DiscordIdAlreadyUsedException, IdAlreadyAssignedException {
        if (playerRepository.existsByDiscordId(player.getDiscordId())) {
            throw new DiscordIdAlreadyUsedException(player.getDiscordId());
        }

        if (player.isAssignedId()) {
            throw new IdAlreadyAssignedException(player.getId());
        }

        player = playerRepository.save(player);

        log.info("Created player {}", player);
        return player;
    }
}
