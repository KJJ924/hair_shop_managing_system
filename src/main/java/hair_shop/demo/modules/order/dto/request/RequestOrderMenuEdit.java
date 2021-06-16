package hair_shop.demo.modules.order.dto.request;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestOrderMenuEdit {

    @NotNull
    @ApiModelProperty(value = "메뉴이름", required = true, example = "다운펌")
    private String menuName;

    @NotNull
    @ApiModelProperty(value = "주문번호", required = true, example = "1")
    private Long orderId;
}
