package hair_shop.demo.modules.order.dto.request;

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

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime reservationStart;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime reservationEnd;


    public boolean isAfter() {
        return reservationStart.isAfter(this.reservationEnd);
    }
}
