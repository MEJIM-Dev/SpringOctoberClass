package First.Spring.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class NigerianNumberValidator implements ConstraintValidator<NigerianNumber,String> {

    private long min;
    private long max;
    private String condition;
    private String[] countryCodes;

    @Override
    public void initialize(NigerianNumber constraintAnnotation) {
        this.max = constraintAnnotation.max();
        this.min = constraintAnnotation.min();
        this.condition = constraintAnnotation.condition();
        this.countryCodes = constraintAnnotation.countryCodes();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value==null || value.length()<min || value.length()>max){
            return false;
        }
        if(value.length()==10){
            Pattern compile = Pattern.compile(condition);
            return Pattern.matches(compile.pattern(),value);
        }
        for (String conCode: countryCodes) {
            Pattern compile = Pattern.compile(conCode+condition);
        }
//        if(value.length()==11){
//            Pattern compile = Pattern.compile(countryCodes[0]+condition);
//            return Pattern.matches(compile.pattern(),value);
////            return value.matches("^0[0-9]{10}");
//        }
//        if(value.length()==13){
//            return value.matches("234[0-9]{10}$");
//        }
//        if(value.length()==14){
//            return value.matches("\\+234[0-9]{10}");
//        }
        return false;
    }
}
