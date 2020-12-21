package hair_shop.apiMessage;
import lombok.*;

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

}