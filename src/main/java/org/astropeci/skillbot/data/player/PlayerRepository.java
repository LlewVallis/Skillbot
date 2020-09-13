package org.astropeci.skillbot.data.player;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByDiscordId(String discordId);

    boolean existsByDiscordId(String discordId);

    Set<Player> findByDisplayNameContainingIgnoreCase(String partialName);

    Set<Player> findAllByDisplayNameIn(Set<String> names);
}
