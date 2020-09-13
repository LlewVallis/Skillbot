package org.astropeci.skillbot.request.match;

import org.astropeci.skillbot.data.match.Match;
import org.astropeci.skillbot.data.match.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Requests data about a specific match, based on its ID.
 */
@Component
public class GetMatchRequest {

    @Autowired
    private MatchRepository matchRepository;

    @Transactional(readOnly = true)
    public Match run(long id) throws NoSuchMatchException {
        return matchRepository.findById(id).orElseThrow(() -> new NoSuchMatchException(id));
    }
}
