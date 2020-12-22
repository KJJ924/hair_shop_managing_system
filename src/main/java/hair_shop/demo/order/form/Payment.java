package hair_shop.demo.order.form;

public enum  Payment {
    CASH("현금"),POINT("포인트"),NOT_PAYMENT("미결제");

    private String value;

    Payment(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
