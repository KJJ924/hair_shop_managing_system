package hair_shop.demo.error;

import lombok.Getter;

/**
 * @author dkansk924@naver.com
 * @since 2021/05/31
 */


@Getter
public class ErrorResponse {

    private final int statusCode;
    private final String message;

    private ErrorResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public static ErrorResponse of(int statusCode, String message) {
        return new ErrorResponse(statusCode, message);
    }

}
