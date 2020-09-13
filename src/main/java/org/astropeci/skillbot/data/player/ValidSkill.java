package org.astropeci.skillbot.data.player;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SkillValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSkill {

    String message() default "invalid skill";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
