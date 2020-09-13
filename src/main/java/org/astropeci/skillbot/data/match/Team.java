package org.astropeci.skillbot.data.match;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Team {
    TEAM_A("teamA"),
    TEAM_B("teamB");

    @Getter(onMethod_ = { @JsonValue })
    private final String jsonName;
}
