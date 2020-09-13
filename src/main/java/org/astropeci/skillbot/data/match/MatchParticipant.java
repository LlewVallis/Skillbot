package org.astropeci.skillbot.data.match;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.astropeci.skillbot.data.player.Player;
import org.astropeci.skillbot.data.player.Skill;
import org.astropeci.skillbot.data.player.ValidSkill;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

/**
 * Data about a player who played in a match, including the player and match themselves.
 */
@Setter
@Getter
@EqualsAndHashCode
@Entity
public class MatchParticipant {

    @Id
    @GeneratedValue
    @NotNull
    @PositiveOrZero
    private Long id;

    @ManyToOne
    @NotNull
    private Player player;

    @ManyToOne
    @NotNull
    @JsonIgnore
    @JoinColumn(name = "match_id")
    @EqualsAndHashCode.Exclude
    private Match match;

    @NotNull
    @Embedded
    @JsonUnwrapped(suffix = "Before")
    @AttributeOverrides({
            @AttributeOverride(name = "trueskill", column = @Column(name = "trueskill_before")),
            @AttributeOverride(name = "deviation", column = @Column(name = "deviation_before"))
    })
    @ValidSkill
    private Skill skillBefore;

    @NotNull
    @Embedded
    @JsonUnwrapped(suffix = "After")
    @AttributeOverrides({
            @AttributeOverride(name = "trueskill", column = @Column(name = "trueskill_after")),
            @AttributeOverride(name = "deviation", column = @Column(name = "deviation_after"))
    })
    @ValidSkill
    private Skill skillAfter;

    @NotNull
    @Enumerated
    private Team team;

    private MatchParticipant() { }

    public MatchParticipant(Player player, Match match, Skill skillBefore, Skill skillAfter, Team team) {
        this.player = player;
        this.match = match;
        this.skillBefore = skillBefore;
        this.skillAfter = skillAfter;
        this.team = team;
    }

    @Override
    public String toString() {
        return "%s %s -> %s (%s)".formatted(player.getDisplayName(), skillBefore, skillAfter, player.getDiscordId());
    }
}
