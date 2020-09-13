package org.astropeci.skillbot.data.match;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.astropeci.skillbot.data.player.Player;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Data about a match which has occurred, linking to its {@link MatchParticipant}s.
 */
@Getter
@Entity
@EqualsAndHashCode
public class Match {

    @Id
    @GeneratedValue
    @NotNull
    @PositiveOrZero
    private Long id;

    @NotNull
    private MatchOutcome outcome;

    /**
     * The approximate time the match occurred.
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    @NotNull
    private Date time;

    @NotNull
    @OneToMany(
            mappedBy = "match",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private Set<@NotNull MatchParticipant> participants = new HashSet<>();

    private Match() { }

    public Match(MatchOutcome outcome, Date time) {
        this.outcome = outcome;
        this.time = time;
    }

    /**
     * Finds all participants in team A.
     */
    public Set<MatchParticipant> getTeamA() {
        return participants.stream()
                .filter(participant -> participant.getTeam() == Team.TEAM_A)
                .collect(Collectors.toSet());
    }

    /**
     * Finds all participants in team B.
     */
    public Set<MatchParticipant> getTeamB() {
        return participants.stream()
                .filter(participant -> participant.getTeam() == Team.TEAM_B)
                .collect(Collectors.toSet());
    }

    private String getTeamString(Set<MatchParticipant> players) {
        return players.stream()
                .map(MatchParticipant::getPlayer)
                .map(Player::getDisplayName)
                .collect(Collectors.joining(","));
    }

    @JsonIgnore
    public boolean isAssignedId() {
        return id != null;
    }

    @Override
    public String toString() {
        String teamA = getTeamString(getTeamA());
        String teamB = getTeamString(getTeamB());
        String outcome = getOutcome() == MatchOutcome.TEAM_A_WINS ? "wins against" : "ties with";

        return "%s %s %s (%s)".formatted(teamA, outcome, teamB, id);
    }
}
