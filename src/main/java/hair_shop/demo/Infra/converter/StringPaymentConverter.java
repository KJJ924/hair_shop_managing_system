package hair_shop.demo.Infra.converter;

import hair_shop.demo.modules.order.form.Payment;
import org.springframework.core.convert.converter.Converter;

public class StringPaymentConverter implements Converter<String, Payment> {
    @Override
    public Payment convert(String s) {
        return Payment.valueOf(s.toUpperCase());
    }

}
