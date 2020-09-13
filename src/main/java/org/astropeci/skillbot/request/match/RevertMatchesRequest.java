package org.astropeci.skillbot.request.match;

import lombok.Value;
import org.astropeci.skillbot.data.match.Match;
import org.astropeci.skillbot.data.match.MatchParticipant;
import org.astropeci.skillbot.data.match.MatchRepository;
import org.astropeci.skillbot.data.player.Player;
import org.astropeci.skillbot.data.player.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Request for an amount of matches to be undone, propagating any changes fully.
 *
 * Only matches from the top of the history stack can be undone.
 */
@Component
public class RevertMatchesRequest {

    public static final int MAX_REVERT_AMOUNT = 25;

    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private PlayerRepository playerRepository;

    @Value
    private static class IdEqualityPlayer {

        Player player;

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof IdEqualityPlayer)) {
                return false;
            }

            return ((IdEqualityPlayer) other).player.getId().equals(player.getId());
        }

        @Override
        public int hashCode() {
            return Long.hashCode(player.getId());
        }
    }

    @Transactional
    public List<Match> run(int amount) throws InsufficientHistoryException, RevertTooLargeException {
        if (amount < 0) {
            throw new IllegalArgumentException("amount cannot be lesser than zero");
        }

        if (amount > MAX_REVERT_AMOUNT) {
            throw new RevertTooLargeException(MAX_REVERT_AMOUNT);
        }

        List<Match> matchList = matchRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        try {
            matchList = matchList.subList(0, amount);
        } catch (IndexOutOfBoundsException e) {
            throw new InsufficientHistoryException(matchList.size());
        }

        Set<IdEqualityPlayer> playersToSave = new HashSet<>();

        for (Match match : matchList) {
            for (MatchParticipant participant : match.getParticipants()) {
                Player player = participant.getPlayer();

                player.setSkill(participant.getSkillBefore().clone());

                playersToSave.add(new IdEqualityPlayer(player));
            }
        }

        playerRepository.saveAll(playersToSave.stream()
                .map(IdEqualityPlayer::getPlayer)
                .collect(Collectors.toSet()));

        matchRepository.deleteAll(matchList);

        return matchList;
    }
}
