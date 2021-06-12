package hair_shop.demo.modules.order.domain;

public enum Payment {
    CASH("현금"),
    POINT("포인트"),
    NOT_PAYMENT("미결제");

    private final String value;

    Payment(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
