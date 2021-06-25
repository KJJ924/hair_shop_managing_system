package hair_shop.demo.infra.vaild.annotation;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/24
 */

public class StartAndEndTimeCheckValidator implements
    ConstraintValidator<StartAndEndTimeCheck, Object> {

    private String message;
    private String startDate;
    private String endDate;

    @Override
    public void initialize(StartAndEndTimeCheck constraintAnnotation) {
        message = constraintAnnotation.message();
        startDate = constraintAnnotation.startDate();
        endDate = constraintAnnotation.endDate();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context) {
        try {
            int invalidCount = 0;
            LocalDateTime reservationStart = (LocalDateTime) getFieldValue(o, startDate);
            LocalDateTime reservationEnd = (LocalDateTime) getFieldValue(o, endDate);
            if (reservationStart.isAfter(reservationEnd)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(startDate)
                    .addConstraintViolation();
                invalidCount += 1;
            }
            return invalidCount == 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Object getFieldValue(Object object, String fieldName) throws Exception {
        Class<?> clazz = object.getClass();
        Field dateField = clazz.getDeclaredField(fieldName);
        dateField.setAccessible(true);
        return dateField.get(object);
    }
}
