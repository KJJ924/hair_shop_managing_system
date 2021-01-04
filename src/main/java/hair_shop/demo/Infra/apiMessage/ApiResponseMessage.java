package hair_shop.demo.Infra.apiMessage;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

@Data
@Builder
public class ApiResponseMessage {
    // HttpStatus
    private String status;
    // Http Default Message
    private String message;
    // Reject Value
    private String rejectedValue;
    // Error field
    private String errorField;
    // Error Code
    private String errorCode;
    // Error Message to USER
    private String errorMessage;

    public static ResponseEntity<Object> error(String rejectedValue, String s) {
        return ResponseEntity.badRequest().body(
                ApiResponseMessage.builder()
                        .status("400")
                        .rejectedValue(rejectedValue)
                        .errorCode("400")
                        .errorMessage(s).build());
    }

    public static ResponseEntity<Object> error(Errors errors) {
        FieldError fieldError = errors.getFieldError();
        assert fieldError != null;
        return ResponseEntity.badRequest().body(
                ApiResponseMessage.builder()
                        .status("400")
                        .rejectedValue(fieldError.getCode())
                        .errorField(fieldError.getField())
                        .errorCode("400")
                        .errorMessage(fieldError.getDefaultMessage()).build());
    }

    public static ResponseEntity<Object> success(String message) {
        return ResponseEntity.ok(
                ApiResponseMessage.builder()
                        .status("200")
                        .message(message).build());
    }

}