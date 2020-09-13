package org.astropeci.skillbot.data.player;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * The skill of a player, measured in trueskill and deviation.
 */
@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class Skill implements Cloneable {

    @NotNull
    private Double trueskill;

    @NotNull
    private Double deviation;

    private Skill() { }

    public Skill(Double trueskill, Double deviation) {
        this.trueskill = trueskill;
        this.deviation = deviation;
    }

    /**
     * Replaces any missing properties with the values from another skill.
     */
    public void patchFrom(Skill other) {
        if (trueskill == null) trueskill = other.trueskill;
        if (deviation == null) deviation = other.deviation;
    }

    @Override
    public String toString() {
        return String.format("%.1f/%.1f", trueskill, deviation);
    }

    @Override
    public Skill clone() {
        return new Skill(trueskill, deviation);
    }
}
