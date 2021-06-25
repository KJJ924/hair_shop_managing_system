package hair_shop.demo.modules.order.dto.request;

import hair_shop.demo.modules.order.domain.Payment;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestPayment {

    @ApiModelProperty(value = "결제 방식", required = true, example = "CASH")
    @NotBlank
    private Payment payment;

    @ApiModelProperty(value = "주문번호", required = true, example = "1")
    @NotBlank
    private Long orderId;

    @Builder
    private RequestPayment(Payment payment, Long orderId) {
        this.payment = payment;
        this.orderId = orderId;
    }
}
