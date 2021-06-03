package hair_shop.demo.modules.order;

import hair_shop.demo.Infra.apiMessage.ApiResponseMessage;
import hair_shop.demo.modules.designer.repository.DesignerRepository;
import hair_shop.demo.modules.designer.domain.Designer;
import hair_shop.demo.modules.member.MemberController;
import hair_shop.demo.modules.member.MemberRepository;
import hair_shop.demo.modules.member.domain.Member;
import hair_shop.demo.modules.menu.repository.MenuRepository;
import hair_shop.demo.modules.menu.domain.Menu;
import hair_shop.demo.modules.order.domain.OrderTable;
import hair_shop.demo.modules.order.form.*;
import hair_shop.demo.modules.order.form.edit.OrderMenuEditForm;
import hair_shop.demo.modules.order.form.edit.OrderTimeEditForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
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
        return orderRepository.save(makeOrder(orderForm));
    }

    private OrderTable makeOrder(OrderForm orderForm){
        Designer designer =designerRepository.findByName(orderForm.getDesignerName());
        Member member = memberRepository.findWithPhoneByPhone(orderForm.getMemberPhoneNumber());
        //FIXME .get()
        Menu menu= menuRepository.findByName(orderForm.getMenuName()).get();
        HashSet<Menu> menus = new HashSet<>();
        menus.add(menu);
        return OrderTable.builder()
                .payment(Payment.NOT_PAYMENT)
                .menus(menus)
                .designers(designer)
                .member(member)
                .reservationDate(LocalDate.from(orderForm.getReservationStart()))
                .reservationStart(orderForm.getReservationStart())
                .reservationEnd(orderForm.getReservationEnd())
                .build();
    }

    public List<MonthData> getMonthData(LocalDate from, LocalDate to) {
        List<OrderTable> orderList = orderRepository.findByMonthDate(from,to);
        Map<Integer, List<OrderTable>> daySeparated = OrderTable.daySeparated(orderList);
        return MonthData.remakeMonthData(daySeparated);
    }


    public Map<Integer, List<OrderTable>> getWeekData(LocalDate from, LocalDate to) {
        List<OrderTable> orderList = orderRepository.findByReservationDateBetweenOrderByReservationDate(from, to);
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
        if (Objects.nonNull(error)) return error;

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

    public void editTime(OrderTimeEditForm orderTimeEditForm) {
        //앞쪽의 @Valid 에서  Null 체크 해서 .get 으로 바로 꺼내도 안전함
        OrderTable orderTable = orderRepository.findById(orderTimeEditForm.getId()).get();
        orderTable.setReservationStart(orderTimeEditForm.getReservationStart());
        orderTable.setReservationEnd(orderTimeEditForm.getReservationEnd());
    }

    public void editMenu(OrderMenuEditForm orderMenuEditForm, String action) {
        if(action.equals("add")) addMenu(orderMenuEditForm);
        if(action.equals("delete")) deleteMenu(orderMenuEditForm);
    }

    private void deleteMenu(OrderMenuEditForm orderMenuEditForm) {
        OrderTable orderTable = orderRepository.findById(orderMenuEditForm.getOrderId()).get();
        //FIXME .get()
        orderTable.menuDelete(menuRepository.findByName(orderMenuEditForm.getMenuName()).get());
    }

    private void addMenu(OrderMenuEditForm orderMenuEditForm) {
        OrderTable orderTable = orderRepository.findById(orderMenuEditForm.getOrderId()).get();
        //앞에 @Valid 에서 메뉴가 있는지 검증이 끝나서 따로 검증하지 않아도 됨
        //FIXME .get()
        orderTable.menuAdd(menuRepository.findByName(orderMenuEditForm.getMenuName()).get());
    }
}
