package hair_shop.demo.infra.vaild.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/24
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartAndEndTimeCheckValidator.class)
public @interface StartAndEndTimeCheck {

    String message() default "에약시작 시간이 예약종료 시간 보다 늦을수 없습니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String startDate();

    String endDate();
}
