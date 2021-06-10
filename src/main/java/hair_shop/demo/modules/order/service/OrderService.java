package hair_shop.demo.modules.order.service;

import hair_shop.demo.Infra.apiMessage.ApiResponseMessage;
import hair_shop.demo.modules.designer.domain.Designer;
import hair_shop.demo.modules.designer.service.DesignerService;
import hair_shop.demo.modules.member.controller.MemberController;
import hair_shop.demo.modules.member.domain.Member;
import hair_shop.demo.modules.member.service.MemberService;
import hair_shop.demo.modules.menu.domain.Menu;
import hair_shop.demo.modules.menu.service.MenuService;
import hair_shop.demo.modules.order.domain.OrderTable;
import hair_shop.demo.modules.order.domain.Payment;
import hair_shop.demo.modules.order.dto.MonthData;
import hair_shop.demo.modules.order.dto.request.OrderForm;
import hair_shop.demo.modules.order.dto.request.RequestOrderMenuEdit;
import hair_shop.demo.modules.order.dto.request.PaymentForm;
import hair_shop.demo.modules.order.dto.request.RequestOrderTimeEdit;
import hair_shop.demo.modules.order.dto.response.ResponseOrder;
import hair_shop.demo.modules.order.exception.NotFoundOrderException;
import hair_shop.demo.modules.order.exception.PaidReservationException;
import hair_shop.demo.modules.order.exception.TimeOverReservationStartException;
import hair_shop.demo.modules.order.repository.OrderRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuService menuService;
    private final DesignerService designerService;
    private final MemberService memberService;

    public ResponseOrder saveOrder(OrderForm orderForm) {
        Designer designer = designerService.findByName(orderForm.getDesignerName());
        Member member = memberService.findByPhone(orderForm.getMemberPhoneNumber());
        Menu menu = menuService.getMenu(orderForm.getMenuName());

        if (orderForm.isAfter()) {
            throw new TimeOverReservationStartException();
        }

        OrderTable order = OrderTable.builder()
            .designers(designer)
            .member(member)
            .reservationStart(orderForm.getReservationStart())
            .reservationEnd(orderForm.getReservationEnd())
            .build();
        order.menuAdd(menu);

        member.addOrder(orderRepository.save(order));

        return ResponseOrder.toMapper(order);
    }

    public ResponseOrder getOrder(Long orderId) {
        OrderTable order = findByOrderId(orderId);
        return ResponseOrder.toMapper(order);
    }

    public List<MonthData> getMonthData(LocalDate from, LocalDate to) {
        List<OrderTable> orderList = orderRepository.findByMonthDate(from, to);
        Map<Integer, List<OrderTable>> daySeparated = OrderTable.daySeparated(orderList);
        return MonthData.remakeMonthData(daySeparated);
    }

    public Map<Integer, List<OrderTable>> getWeekData(LocalDate from, LocalDate to) {
        List<OrderTable> orderList = orderRepository
            .findByCreateAtBetweenOrderByCreateAt(from, to);
        return OrderTable.daySeparated(orderList);
    }

    public ResponseEntity<Object> payment(PaymentForm paymentForm) {
        Long order_id = paymentForm.getOrder_id();
        OrderTable order = findByOrderId(order_id);
        return paymentFactory(paymentForm, order);
    }

    public ResponseOrder editTime(RequestOrderTimeEdit form) {
        if (form.isAfter()) {
            throw new TimeOverReservationStartException();
        }

        OrderTable order = findByOrderId(form.getId());

        LocalDateTime start = form.getReservationStart();
        LocalDateTime end = form.getReservationEnd();

        order.changeReservationTime(start, end);

        return ResponseOrder.toMapper(order);
    }

    private ResponseEntity<Object> paymentFactory(PaymentForm form, OrderTable order) {
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


    private ResponseEntity<Object> pointAndCashProcess(OrderTable order, PaymentForm form) {
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

    public OrderTable findByOrderId(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(NotFoundOrderException::new);
    }

    public ResponseOrder deleteMenu(RequestOrderMenuEdit requestOrderMenuEdit) {
        OrderTable order = findByOrderId(requestOrderMenuEdit.getOrderId());
        order.menuDelete(menuService.getMenu(requestOrderMenuEdit.getMenuName()));
        return ResponseOrder.toMapper(order);
    }

    public ResponseOrder addMenu(RequestOrderMenuEdit requestOrderMenuEdit) {
        OrderTable order = findByOrderId(requestOrderMenuEdit.getOrderId());
        order.menuAdd(menuService.getMenu(requestOrderMenuEdit.getMenuName()));
        return ResponseOrder.toMapper(order);
    }

    public void deleteOrder(Long orderId) {
        OrderTable order = findByOrderId(orderId);
        if(order.checkPayment()){
            throw new PaidReservationException();
        }
        orderRepository.delete(order);
    }
}
