package hair_shop.demo.infra.converter;

import hair_shop.demo.modules.order.domain.Payment;
import org.springframework.core.convert.converter.Converter;

public class StringPaymentConverter implements Converter<String, Payment> {

    @Override
    public Payment convert(String s) {
        return Payment.valueOf(s.toUpperCase());
    }

}
