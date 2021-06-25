package hair_shop.demo.modules.order.dto.request;

import hair_shop.demo.infra.vaild.annotation.StartAndEndTimeCheck;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
@StartAndEndTimeCheck(startDate = "reservationStart", endDate = "reservationEnd")
public class RequestOrder {

    @NotBlank
    @ApiModelProperty(value = "메뉴이름", required = true, example = "다운펌")
    private String menuName;

    @NotBlank
    @ApiModelProperty(value = "회원 전화번호", required = true, example = "01012345678")
    private String memberPhoneNumber;

    @NotBlank
    @ApiModelProperty(value = "디자이너 이름", required = true, example = "디자이너")
    private String designerName;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull
    @ApiModelProperty(value = "예약 시작시간", required = true, example = "2021-06-16T11:40")
    private LocalDateTime reservationStart;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull
    @ApiModelProperty(value = "예약 끝나는시간", required = true, example = "2021-06-16T12:40")
    private LocalDateTime reservationEnd;

}
