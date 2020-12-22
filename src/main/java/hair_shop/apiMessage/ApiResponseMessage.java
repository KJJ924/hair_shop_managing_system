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

    public static ResponseEntity<Object> createError(String filed, String s) {
        return ResponseEntity.badRequest().body(
                ApiResponseMessage.builder()
                        .errorCode("400")
                        .errorMessage(filed + s).build());
    }

    public static ResponseEntity<Object> saveSuccess() {
        return ResponseEntity.ok(
                ApiResponseMessage.builder()
                        .status("200")
                        .message("성공적으로 저장됨").build());
    }

}