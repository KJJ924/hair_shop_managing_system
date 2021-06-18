package hair_shop.demo.modules.order.payment.service;

import hair_shop.demo.modules.member.domain.Member;
import hair_shop.demo.modules.member.membership.error.NotMemberShipException;
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

        if (payment.equals(Payment.CASH)) {
            return cashPaymentProcess(order);
        }

        return pointPaymentProcess(order);
    }

    private ResponsePayment cashPaymentProcess(Order order) {
        order.cashPayment();
        return ResponsePayment.builder()
            .paymentType(Payment.CASH.getValue())
            .memberName(order.getMemberName())
            .phoneNumber(order.getMemberPhone())
            .build();
    }

    private ResponsePayment pointPaymentProcess(Order order) {
        paymentValidation(order.getMember());

        int remainingPoint = order.pointPayment();

        return ResponsePayment.builder()
            .paymentType(Payment.POINT.getValue())
            .memberName(order.getMemberName())
            .phoneNumber(order.getMemberPhone())
            .holdingPoint(order.totalPrice() + remainingPoint)
            .paymentAmount(order.totalPrice())
            .remainingPoint(remainingPoint)
            .build();
    }

    private void paymentValidation(Member member) {
        if (!member.isMemberShip()) {
            throw new NotMemberShipException();
        }
    }
}
