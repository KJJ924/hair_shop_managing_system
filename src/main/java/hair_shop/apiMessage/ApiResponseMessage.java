package hair_shop.apiMessage;
import lombok.*;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ApiResponseMessage {
    // HttpStatus
    private String status;
    // Http Default Message
    private String message;
    // Error Code
    private String errorCode;
    // Error Message to USER
    private String errorMessage;

    public static ResponseEntity<Object> error(String filed, String s) {
        return ResponseEntity.badRequest().body(
                ApiResponseMessage.builder()
                        .errorCode("400")
                        .errorMessage(filed + s).build());
    }

    public static ResponseEntity<Object> success(String message) {
        return ResponseEntity.ok(
                ApiResponseMessage.builder()
                        .status("200")
                        .message(message).build());
    }

}