package First.Spring.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.Map;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = NigerianNumberValidator.class)
public @interface NigerianNumber {

    String message() default "Invalid Number";

    long min() default 5;

    long max() default 15;

    String[] countryCodes() default {"","0","234","+234"};

    String condition() default "[0-9]{10}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
