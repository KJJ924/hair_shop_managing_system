package hair_shop.demo.order.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class OrderForm {

    @NotBlank
    private String menuName;
    @NotBlank
    private String memberPhoneNumber;
    @NotBlank
    private String designerName;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reservationStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reservationEnd;

}
