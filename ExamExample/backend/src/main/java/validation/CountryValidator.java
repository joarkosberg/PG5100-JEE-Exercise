package validation;

import enums.CountryName;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CountryValidator implements ConstraintValidator<Country, CountryName> {
       public void initialize(Country constraint) {

       }

       public boolean isValid(CountryName country, ConstraintValidatorContext context) {
          //Already checked with enum anotation, but made for exam purpose
          return true;
       }
}
