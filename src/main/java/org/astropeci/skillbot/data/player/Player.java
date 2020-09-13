package org.astropeci.skillbot.data.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

/**
 * A player who is signed up with their Discord ID.
 */
@Getter
@Setter
@EqualsAndHashCode
@Entity
public class Player {

    /**
     * Internal ID.
     */
    @Id
    @GeneratedValue
    @PositiveOrZero
    private Long id = null;

    /**
     * Customizable non-unique name.
     */
    @Column(length = 32)
    @Length(max = 32, message = "player names cannot be more than 32 characters long")
    @Pattern(regexp = "[-_A-Za-z0-9]+", message = "player names can only contain letters, numbers, hyphens and underscores")
    @NotBlank(message = "player names can't be blank")
    private String displayName;

    /**
     * ID used by the Discord API for the player's account.
     */
    @Column(unique = true)
    @NotNull
    private String discordId;

    @NotNull
    @Embedded
    @JsonUnwrapped
    @ValidSkill
    private Skill skill;

    private Player() { }

    public Player(String displayName, String discordId, Skill skill) {
        this.displayName = displayName;
        this.discordId = discordId;
        this.skill = skill;
    }

    /**
     * Replaces any missing properties with the values from another player.
     */
    public void patchFrom(Player other) {
        if (id == null) id = other.id;
        if (displayName == null) displayName = other.displayName;
        if (discordId == null) discordId = other.discordId;

        if (skill == null) {
            skill = other.skill.clone();
        } else {
            skill.patchFrom(other.skill);
        }
    }

    /**
     * Whether or not the player has an ID yet.
     *
     * New players won't have an ID until they are saved to the repository.
     */
    @JsonIgnore
    public boolean isAssignedId() {
        return id != null;
    }

    @Override
    public String toString() {
        return "%s %s (%s)".formatted(displayName, skill, discordId);
    }
}
