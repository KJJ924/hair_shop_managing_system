package hair_shop.demo.modules.order.payment;

import hair_shop.demo.Infra.apiMessage.ApiResponseMessage;
import hair_shop.demo.modules.member.controller.MemberController;
import hair_shop.demo.modules.member.domain.Member;
import hair_shop.demo.modules.order.domain.OrderTable;
import hair_shop.demo.modules.order.domain.Payment;
import hair_shop.demo.modules.order.dto.request.RequestPayment;
import java.util.Objects;
import javax.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/11
 */

@Service
@Transactional
public class PaymentService {

    public ResponseEntity<Object> paymentFactory(RequestPayment form, OrderTable order) {
        if (order.checkPayment()) {
            return ApiResponseMessage.error("payment_Complete", "이미 결제가 완료됨");
        }

        Payment payment = form.getPayment();
        if (payment.equals(Payment.CASH)) {
            return cashPaymentProcess(payment, order);
        }
        if (payment.equals(Payment.POINT)) {
            return pointPaymentProcess(payment, order);
        }
        if (payment.equals(Payment.CASH_AND_POINT)) {
            return pointAndCashProcess(order, form);
        }

        return ApiResponseMessage.error("notSupportPayment", "지원하지않는 결제방법입니다");
    }

    private ResponseEntity<Object> cashPaymentProcess(Payment payment, OrderTable order) {
        order.setPayment(payment);
        order.getMember().registerVisitDate();
        return ApiResponseMessage.success("결제가 완료됨");
    }

    private ResponseEntity<Object> pointPaymentProcess(Payment payment, OrderTable order) {
        Member member = order.getMember();

        ResponseEntity<Object> error = paymentValidation(member);
        if (error != null) {
            return error;
        }

        Integer savePoint = member.getMemberShipPoint() - order.totalPrice();

        if (!payment.isPayment(savePoint)) {
            return ApiResponseMessage.error(String.valueOf(savePoint), "잔액이 부족합니다");
        }

        paymentSave(order, payment, savePoint);

        return ApiResponseMessage.success("결제가 완료됨");
    }


    private ResponseEntity<Object> pointAndCashProcess(OrderTable order, RequestPayment form) {
        Member member = order.getMember();
        Payment payment = form.getPayment();

        ResponseEntity<Object> error = paymentValidation(member);
        if (Objects.nonNull(error)) {
            return error;
        }

        int resultMenuPrice = order.totalPrice() - form.getCash();

        Integer savePoint = member.getMemberShipPoint() - resultMenuPrice;

        if (!payment.isPayment(savePoint)) {
            return ApiResponseMessage.error(String.valueOf(savePoint), "잔액이 부족합니다");
        }

        paymentSave(order, payment, savePoint);

        return ApiResponseMessage.success("결제가 완료됨");
    }

    private ResponseEntity<Object> paymentValidation(Member member) {
        if (!member.isMemberShip()) {
            return ApiResponseMessage.error("NO MemberShip", MemberController.NOT_MEMBERSHIP);
        }
        return null;
    }

    private void paymentSave(OrderTable order, Payment payment, Integer savePoint) {
        Member member = order.getMember();
        order.setPayment(payment);
        member.getMemberShip().setPoint(savePoint);
        member.registerVisitDate();
    }
}
