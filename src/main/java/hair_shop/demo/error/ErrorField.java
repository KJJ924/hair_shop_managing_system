package hair_shop.demo.error;

import java.util.Objects;
import lombok.Getter;
import org.springframework.validation.FieldError;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/25
 */

@Getter
public class ErrorField {

    private final String field;
    private final String value;
    private final String reason;

    private ErrorField(FieldError fieldError) {
        this.field = fieldError.getField();
        this.value = Objects.requireNonNull(fieldError.getRejectedValue()).toString();
        this.reason = fieldError.getDefaultMessage();
    }

    public static ErrorField mapper(FieldError fieldError) {
        return new ErrorField(fieldError);
    }
}
