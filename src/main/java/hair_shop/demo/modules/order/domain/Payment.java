package hair_shop.demo.modules.order.domain;

import hair_shop.demo.modules.order.payment.dto.response.ResponsePayment;
import java.util.function.Function;

public enum Payment {
    CASH("현금", order -> {
        order.cashPayment();
        return ResponsePayment.builder()
            .paymentType("현금")
            .memberName(order.getMemberName())
            .phoneNumber(order.getMemberPhone())
            .build();
    }),

    POINT("포인트", order -> {
        int remainingPoint = order.pointPayment();

        return ResponsePayment.builder()
            .paymentType("포인트")
            .memberName(order.getMemberName())
            .phoneNumber(order.getMemberPhone())
            .holdingPoint(order.totalPrice() + remainingPoint)
            .paymentAmount(order.totalPrice())
            .remainingPoint(remainingPoint)
            .build();
    }
    ),

    NOT_PAYMENT("미결제", order -> ResponsePayment.builder().build());

    private final String value;
    private final Function<Order, ResponsePayment> paymentFunction;

    Payment(String value, Function<Order, ResponsePayment> paymentFunction) {
        this.value = value;
        this.paymentFunction = paymentFunction;
    }
    public ResponsePayment process(Order order) {
        return paymentFunction.apply(order);
    }

    public String getValue() {
        return value;
    }


}
