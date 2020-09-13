package org.astropeci.skillbot.data.match;

import org.astropeci.skillbot.data.player.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchParticipantRepository extends JpaRepository<MatchParticipant, Long> {

    boolean existsByPlayer(Player player);
}
