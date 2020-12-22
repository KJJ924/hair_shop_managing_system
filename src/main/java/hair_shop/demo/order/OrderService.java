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
        List<OrderTable> orderList = orderRepository.findByReservationStartBetween(standardMonth,plusMonth);
        Map<Integer, List<OrderTable>> daySeparated = OrderTable.daySeparated(orderList);
        return MonthData.remakeMonthData(daySeparated);
    }


    public Map<Integer, List<OrderTable>> getWeekData(LocalDateTime standardDay, LocalDateTime plusDay) {
        List<OrderTable> orderList = orderRepository.findByReservationStartBetween(standardDay, plusDay);
        return OrderTable.daySeparated(orderList);
    }

    public ResponseEntity<Object> payment(Long id, Payment payment) {
        Optional<OrderTable> order = orderRepository.findById(id);
        if(order.isEmpty()){
            return ApiResponseMessage.createError(id.toString(),OrderController.NOT_FOUND_ORDER);
        }
        return paymentProcess(payment, order.get());
    }

    private ResponseEntity<Object> paymentProcess(Payment payment,OrderTable order) {
        if(!payment.equals(Payment.NOT_PAYMENT)){
            return ApiResponseMessage.createError("payment_Complete","이미 결제가 완료됨");
        }
        if(payment.equals(Payment.CASH)){
            return cashPaymentProcess(payment, order);
        }

        Member member = order.getMember();
        if(!member.isMemberShip()){
            return ApiResponseMessage.createError("null", MemberController.NOT_MEMBERSHIP);
        }

        Integer point = member.getMemberShipPoint();
        Integer totalPrice = order.totalPrice();

        if(point <totalPrice){
            return ApiResponseMessage.createError(String.valueOf(point),"잔액이 부족합니다");
        }

        order.setPayment(payment);
        member.getMemberShip().setPoint(point -totalPrice);

        return ResponseEntity.ok().build();
    }


    private ResponseEntity<Object> cashPaymentProcess(Payment payment, OrderTable order) {
        order.setPayment(payment);
        return ResponseEntity.ok().build();
    }
}
