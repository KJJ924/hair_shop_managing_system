package hair_shop.demo.order.form;

public enum  Payment {
    CASH("현금"),POINT("포인트"),NOT_PAYMENT("미결제"),
    CASH_AND_POINT("현금+포인트");

    private String value;

    Payment(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
