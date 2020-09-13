package org.astropeci.skillbot.data.match;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The winner of a match.
 *
 * Team B is never internally stored as the winner, so that option is not present.
 */
@RequiredArgsConstructor
public enum MatchOutcome {
    TEAM_A_WINS("teamAWins"),
    TIE("tie");

    @Getter(onMethod_ = { @JsonValue })
    private final String jsonName;
}
