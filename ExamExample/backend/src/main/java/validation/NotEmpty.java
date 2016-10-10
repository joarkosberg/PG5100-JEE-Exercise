package validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@NotNull
@Constraint(validatedBy = NotEmptyValidator.class)
@Target({  //tells on what the @ annotation can be used
        ElementType.FIELD,
        ElementType.METHOD,
        ElementType.PARAMETER,
        ElementType.CONSTRUCTOR,
        ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME) //specify it should end up in the bytecode and be readable using reflection
@Documented //should be part of the JavaDoc of where it is applied to
public @interface NotEmpty {
    //these 3 are default needed methods
    String message() default "Cant be empty";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
