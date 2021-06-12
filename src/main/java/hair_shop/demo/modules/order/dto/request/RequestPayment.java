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
    private Long orderId;

    @Builder
    private RequestPayment(Payment payment, Long orderId) {
        this.payment = payment;
        this.orderId = orderId;
    }
}
