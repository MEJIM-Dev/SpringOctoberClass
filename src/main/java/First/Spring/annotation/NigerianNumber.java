package First.Spring.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = NigerianNumberValidator.class)
public @interface NigerianNumber {

    String message() default "Invalid Number";

    String[] countryCodes() default {"+234","234","0"};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
