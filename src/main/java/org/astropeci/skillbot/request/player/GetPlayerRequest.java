package org.astropeci.skillbot.request.player;

import org.astropeci.skillbot.data.player.Player;
import org.astropeci.skillbot.data.player.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Gets data about a specific player.
 */
@Component
public class GetPlayerRequest {

    @Autowired
    private PlayerRepository playerRepository;

    @Transactional(readOnly = true)
    public Player run(long id) throws NoSuchPlayerException {
        return playerRepository.findById(id).orElseThrow(() -> new NoSuchPlayerException(id));
    }
}
