package hair_shop.demo.infra.vaild.annotation;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerErrorException;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/24
 */

@Slf4j
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

        int invalidCount = 0;
        LocalDateTime reservationStart = getFieldValue(o, startDate);
        LocalDateTime reservationEnd = getFieldValue(o, endDate);
        if (reservationStart.isAfter(reservationEnd)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(startDate)
                .addConstraintViolation();
            invalidCount += 1;
        }
        return invalidCount == 0;
    }

    private LocalDateTime getFieldValue(Object object, String fieldName) {
        Class<?> clazz = object.getClass();
        Field dateField;
        try {
            dateField = clazz.getDeclaredField(fieldName);
            dateField.setAccessible(true);
            Object target = dateField.get(object);
            if (!(target instanceof LocalDateTime)) {
                throw new ClassCastException("casting exception");
            }
            return (LocalDateTime) target;
        } catch (NoSuchFieldException e) {
            log.error("NoSuchFieldException", e);
        } catch (IllegalAccessException e) {
            log.error("IllegalAccessException", e);
        }
        throw new ServerErrorException("Not Found Field");
    }
}
