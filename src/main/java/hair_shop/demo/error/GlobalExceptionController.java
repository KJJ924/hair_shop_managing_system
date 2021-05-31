package hair_shop.demo.error;

import hair_shop.demo.error.exception.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author dkansk924@naver.com
 * @since 2021/05/31
 */


@ControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> businessError(BusinessException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ErrorResponse response = ErrorResponse
            .of(errorCode.getStatus().value(), errorCode.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }
}
