package hair_shop.demo.config.converter;

import hair_shop.demo.order.form.Payment;
import org.springframework.core.convert.converter.Converter;

public class StringPaymentConverter implements Converter<String, Payment> {
    @Override
    public Payment convert(String s) {
        return Payment.valueOf(s.toUpperCase());
    }

}
