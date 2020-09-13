package org.astropeci.skillbot.request.match;

import org.astropeci.skillbot.data.match.Match;
import org.astropeci.skillbot.data.match.MatchRepository;
import org.astropeci.skillbot.request.PagedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Gets a paginated list of every match that has occurred, in order of most to least recent.
 */
@Component
public class ListMatchesRequest {

    public static final int PAGE_SIZE = 10;

    @Autowired
    private MatchRepository matchRepository;

    @Transactional(readOnly = true)
    public PagedResponse<Match> run(int page) {
        if (page < 0) {
            throw new IllegalArgumentException("page cannot be negative");
        }

        List<Match> elements = matchRepository.findAll(PageRequest.of(page, PAGE_SIZE, Sort.Direction.DESC, "id")).getContent();

        return new PagedResponse<>(
                elements,
                page,
                PAGE_SIZE,
                (int) matchRepository.count()
        );
    }
}
