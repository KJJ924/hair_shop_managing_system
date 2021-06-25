package hair_shop.demo.error;

import hair_shop.demo.error.exception.BusinessException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> dataValidateError(MethodArgumentNotValidException e) {
        ErrorResponse response = ErrorResponse.builder()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .message("입력값이 올바르지 않습니다.")
            .errors(createMessage(e))
            .build();
        return ResponseEntity.badRequest().body(response);
    }

    private List<ErrorField> createMessage(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        return fieldErrors.stream().map(ErrorField::mapper).collect(Collectors.toList());
    }
}
