package hair_shop.demo.modules.order.dto.request;

import hair_shop.demo.infra.vaild.annotation.StartAndEndTimeCheck;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
@StartAndEndTimeCheck(startDate = "reservationStart", endDate = "reservationEnd")
public class RequestOrderTimeEdit {

    @NotNull
    @ApiModelProperty(value = "주문번호", required = true, example = "1")
    private Long id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull
    @ApiModelProperty(value = "예약 시작시간", required = true, example = "2021-06-16T11:40")
    private LocalDateTime reservationStart;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull
    @ApiModelProperty(value = "예약 끝나는시간", required = true, example = "2021-06-16T12:40")
    private LocalDateTime reservationEnd;

}
