package hair_shop.demo.modules.order.payment.service;

import hair_shop.demo.modules.order.domain.Order;
import hair_shop.demo.modules.order.domain.Payment;
import hair_shop.demo.modules.order.dto.request.RequestPayment;
import hair_shop.demo.modules.order.payment.dto.response.ResponsePayment;
import hair_shop.demo.modules.order.payment.exception.PaymentCompleteException;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/11
 */

@Service
@Transactional
public class PaymentService {

    public ResponsePayment paymentFactory(RequestPayment requestPayment, Order order) {
        if (order.checkPayment()) {
            throw new PaymentCompleteException();
        }
        Payment payment = requestPayment.getPayment();

        return payment.process(order);
    }
}
