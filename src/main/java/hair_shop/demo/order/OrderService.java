package hair_shop.demo.order;

import hair_shop.apiMessage.ApiResponseMessage;
import hair_shop.demo.designer.DesignerRepository;
import hair_shop.demo.domain.*;
import hair_shop.demo.member.MemberController;
import hair_shop.demo.member.MemberRepository;
import hair_shop.demo.menu.MenuRepository;
import hair_shop.demo.order.form.MonthData;
import hair_shop.demo.order.form.OrderForm;
import hair_shop.demo.order.form.Payment;
import hair_shop.demo.order.form.PaymentForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final MenuRepository menuRepository;
    private final DesignerRepository designerRepository;

    public OrderTable saveOrder(OrderForm orderForm) {
        OrderTable order = makeOrder(orderForm);
        order.setPayment(Payment.NOT_PAYMENT);
        return orderRepository.save(order);
    }

    private OrderTable makeOrder(OrderForm orderForm){
        Designer designer =designerRepository.findByName(orderForm.getDesignerName());
        Member member = memberRepository.findWithPhoneByPhone(orderForm.getMemberPhoneNumber());
        Menu menu= menuRepository.findByName(orderForm.getMenuName());
        HashSet<Menu> menus = new HashSet<>();
        menus.add(menu);
        return OrderTable.builder()
                .menus(menus)
                .designers(designer)
                .member(member)
                .reservationStart(orderForm.getReservationStart())
                .reservationEnd(orderForm.getReservationEnd())
                .build();
    }

    public List<MonthData> getMonthData(LocalDateTime standardMonth, LocalDateTime plusMonth) {
        List<OrderTable> orderList = orderRepository.findByReservationStartBetweenOrderByReservationStart(standardMonth,plusMonth);
        Map<Integer, List<OrderTable>> daySeparated = OrderTable.daySeparated(orderList);
        return MonthData.remakeMonthData(daySeparated);
    }


    public Map<Integer, List<OrderTable>> getWeekData(LocalDateTime standardDay, LocalDateTime plusDay) {
        List<OrderTable> orderList = orderRepository.findByReservationStartBetweenOrderByReservationStart(standardDay, plusDay);
        return OrderTable.daySeparated(orderList);
    }

    public ResponseEntity<Object> payment(PaymentForm paymentForm) {
        Long order_id = paymentForm.getOrder_id();

        Optional<OrderTable> order = orderRepository.findById(order_id);
        if(order.isEmpty()) return ApiResponseMessage.error(order_id.toString(),OrderController.NOT_FOUND_ORDER);

        return paymentFactory(paymentForm, order.get());
    }

    private ResponseEntity<Object> paymentFactory(PaymentForm form,OrderTable order) {
        if(order.checkPayment()) return ApiResponseMessage.error("payment_Complete","이미 결제가 완료됨");

        Payment payment = form.getPayment();
        if(payment.equals(Payment.CASH)) return cashPaymentProcess(payment, order);
        if(payment.equals(Payment.POINT)) return pointPaymentProcess(payment, order);
        if(payment.equals(Payment.CASH_AND_POINT)) return pointAndCashProcess(order,form);

        return ApiResponseMessage.error("notSupportPayment","지원하지않는 결제방법입니다");
    }

    private ResponseEntity<Object> cashPaymentProcess(Payment payment, OrderTable order) {
        order.setPayment(payment);
        order.getMember().registerVisitDate();
        return ApiResponseMessage.success("결제가 완료됨");
    }

    private ResponseEntity<Object> pointPaymentProcess(Payment payment, OrderTable order) {
        Member member = order.getMember();

        ResponseEntity<Object> error = paymentValidation(member);
        if (error != null) return error;

        Integer savePoint =member.getMemberShipPoint() -order.totalPrice();

        if(!payment.isPayment(savePoint))
            return ApiResponseMessage.error(String.valueOf(savePoint),"잔액이 부족합니다");

        paymentSave(order,payment,savePoint);

        return ApiResponseMessage.success("결제가 완료됨");
    }



    private ResponseEntity<Object> pointAndCashProcess(OrderTable order, PaymentForm form) {
        Member member = order.getMember();
        Payment payment = form.getPayment();

        ResponseEntity<Object> error = paymentValidation(member);
        if (error != null) return error;

        int resultMenuPrice = order.totalPrice()-form.getCash();

        Integer savePoint  = member.getMemberShipPoint()-resultMenuPrice;

        if(!payment.isPayment(savePoint))
            return ApiResponseMessage.error(String.valueOf(savePoint),"잔액이 부족합니다");

        paymentSave(order,payment,savePoint);

        return ApiResponseMessage.success("결제가 완료됨");
    }

    private ResponseEntity<Object> paymentValidation(Member member) {
        if(!member.isMemberShip()){
            return ApiResponseMessage.error("NO MemberShip", MemberController.NOT_MEMBERSHIP);
        }
        return null;
    }

    private void paymentSave(OrderTable order ,Payment payment,Integer savePoint){
        Member member = order.getMember();
        order.setPayment(payment);
        member.getMemberShip().setPoint(savePoint);
        member.registerVisitDate();
    }

}
