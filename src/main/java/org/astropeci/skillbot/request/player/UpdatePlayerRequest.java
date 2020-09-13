package org.astropeci.skillbot.request.player;

import lombok.extern.slf4j.Slf4j;
import org.astropeci.skillbot.data.player.Player;
import org.astropeci.skillbot.data.player.PlayerRepository;
import org.astropeci.skillbot.request.IdNotAssignedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;

/**
 * Updates the properties of a player.
 */
@Slf4j
@Component
@Validated
public class UpdatePlayerRequest {

    @Autowired
    private PlayerRepository playerRepository;

    @Transactional
    public Player run(@Valid Player player) throws IdNotAssignedException, NoSuchPlayerException {
        if (!player.isAssignedId()) {
            throw new IdNotAssignedException();
        }

        long id = player.getId();

        if (!playerRepository.existsById(id)) {
            throw new NoSuchPlayerException(id);
        }

        player = playerRepository.save(player);

        log.info("Updated player to {}", player);
        return player;
    }
}
