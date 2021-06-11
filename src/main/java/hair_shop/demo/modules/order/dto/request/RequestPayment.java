package hair_shop.demo.modules.order.dto.request;

import hair_shop.demo.modules.order.domain.Payment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestPayment {
    private Payment payment;
    private Long order_id;
    private Integer cash;

    @Builder
    private RequestPayment(Payment payment, Long order_id, Integer cash) {
        this.payment = payment;
        this.order_id = order_id;
        this.cash = cash;
    }
}
