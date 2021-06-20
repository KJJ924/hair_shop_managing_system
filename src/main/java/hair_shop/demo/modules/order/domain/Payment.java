package hair_shop.demo.modules.order.domain;

import hair_shop.demo.modules.order.payment.dto.response.ResponsePayment;

public enum Payment {
    CASH("현금") {
        @Override
        public ResponsePayment process(Order order) {
            order.cashPayment();
            return ResponsePayment.builder()
                .paymentType(this.getValue())
                .memberName(order.getMemberName())
                .phoneNumber(order.getMemberPhone())
                .build();
        }
    },
    POINT("포인트") {
        @Override
        public ResponsePayment process(Order order) {
            int remainingPoint = order.pointPayment();
            return ResponsePayment.builder()
                .paymentType(this.getValue())
                .memberName(order.getMemberName())
                .phoneNumber(order.getMemberPhone())
                .holdingPoint(order.totalPrice() + remainingPoint)
                .paymentAmount(order.totalPrice())
                .remainingPoint(remainingPoint)
                .build();
        }
    },
    NOT_PAYMENT("미결제") {
        @Override
        public ResponsePayment process(Order order) {
            return ResponsePayment.builder().build();
        }
    };

    private final String value;

    Payment(String value) {
        this.value = value;
    }

    public abstract ResponsePayment process(Order order);

    public String getValue() {
        return value;
    }


}
