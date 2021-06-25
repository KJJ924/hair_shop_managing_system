package hair_shop.demo.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

/**
 * @author dkansk924@naver.com
 * @since 2021/05/31
 */


@Getter
@JsonInclude(Include.NON_NULL)
public class ErrorResponse {

    private final int statusCode;
    private final String message;
    private List<ErrorField> errors;
    private final LocalDateTime timestamp = LocalDateTime.now();

    private ErrorResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    @Builder
    public ErrorResponse(int statusCode, String message,
        List<ErrorField> errors) {
        this.statusCode = statusCode;
        this.message = message;
        this.errors = errors;
    }

    public static ErrorResponse of(int statusCode, String message) {
        return new ErrorResponse(statusCode, message);
    }

}
