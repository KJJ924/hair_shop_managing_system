package hair_shop.demo.modules.order.form;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentForm {
    private Payment payment;
    private Long order_id;
    private Integer cash;
}
