package no.joarkosberg.exam.backend.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@NotNull
@Constraint(validatedBy = NotEmptyValidator.class)
@Target({
        ElementType.FIELD,
        ElementType.METHOD,
        ElementType.PARAMETER,
        ElementType.CONSTRUCTOR,
        ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotEmpty {
    String message() default "Cant be empty";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
