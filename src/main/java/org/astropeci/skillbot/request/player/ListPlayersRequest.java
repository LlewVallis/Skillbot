package org.astropeci.skillbot.request.player;

import org.astropeci.skillbot.data.player.Player;
import org.astropeci.skillbot.data.player.PlayerRepository;
import org.astropeci.skillbot.request.PagedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Gets a paginated list of all players, allowing for customised sorting.
 */
@Component
public class ListPlayersRequest {

    public static final int PAGE_SIZE = 10;

    @Autowired
    private PlayerRepository playerRepository;

    /**
     * Criterion which can be sorted upon, including an unspecified criteria.
     */
    public enum SortCriterion {
        UNSPECIFIED,
        TRUESKILL,
    }

    @Transactional(readOnly = true)
    public PagedResponse<Player> run(int page, SortCriterion criterion, Sort.Direction direction) {
        if (page < 0) {
            throw new IllegalArgumentException("page cannot be negative");
        }

        Sort sort = null;
        switch (criterion) {
            case UNSPECIFIED:
                sort = Sort.unsorted();
                break;
            case TRUESKILL:
                sort = Sort.by(direction, "skill.trueskill");
                break;
        }

        List<Player> elements = playerRepository.findAll(PageRequest.of(page, PAGE_SIZE, sort)).getContent();

        return new PagedResponse<>(
                elements,
                page,
                PAGE_SIZE,
                (int) playerRepository.count()
        );
    }
}
