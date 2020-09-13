package org.astropeci.skillbot.data.player;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validates constraints on skill values. Fairly lenient.
 */
public class SkillValidator implements ConstraintValidator<ValidSkill, Skill> {

    @Override
    public boolean isValid(Skill skill, ConstraintValidatorContext context) {
        return isValid(skill);
    }

    public boolean isValid(Skill skill) {
        Double trueskill = skill.getTrueskill();
        Double devation = skill.getDeviation();

        if (trueskill == null || devation == null) {
            return false;
        }

        return !trueskill.isNaN() &&
                !devation.isNaN() &&
                !trueskill.isInfinite() &&
                !devation.isInfinite() &&
                (devation > 0);
    }
}
