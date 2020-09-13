package org.astropeci.skillbot.request.player;

import lombok.extern.slf4j.Slf4j;
import org.astropeci.skillbot.data.match.MatchParticipantRepository;
import org.astropeci.skillbot.data.player.Player;
import org.astropeci.skillbot.data.player.PlayerRepository;
import org.astropeci.skillbot.request.IdNotAssignedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

/**
 * Request to remove a player. Will fail if they have completed any matches.
 */
@Slf4j
@Component
public class DeletePlayerRequest {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private MatchParticipantRepository matchParticipantRepository;

    @Transactional
    public Player run(Player player) throws IdNotAssignedException, NoSuchPlayerException, PlayerHasPlayedException {
        if (!player.isAssignedId()) {
            throw new IdNotAssignedException();
        }

        if (!playerRepository.existsById(player.getId())) {
            throw new NoSuchPlayerException(player.getId());
        }

        // FIXME Test
        if (matchParticipantRepository.existsByPlayer(player)) {
            throw new PlayerHasPlayedException(player);
        }

        playerRepository.deleteById(player.getId());
        log.info("Deleted player {}", player);

        return player;
    }
}
