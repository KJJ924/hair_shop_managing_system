package hair_shop.demo.modules.order.form;

import com.fasterxml.jackson.annotation.JsonValue;

public enum  Payment {
    CASH("현금"),POINT("포인트"),NOT_PAYMENT("미결제"),
    CASH_AND_POINT("현금+포인트");

    private String value;

    Payment(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public boolean isPayment(Integer value){
        return value >= 0;
    }

}
