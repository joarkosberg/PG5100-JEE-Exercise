package validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class NotEmptyValidator implements ConstraintValidator<NotEmpty, String> {
        @Override
        public void initialize(NotEmpty constraintAnnotation) {
        }

        public boolean isValid(String input, ConstraintValidatorContext context) {
            if(input == null){
                return false;
            }

            return !input.trim().isEmpty();
        }
}
