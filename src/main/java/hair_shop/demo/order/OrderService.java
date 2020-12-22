package hair_shop.demo.order;

import hair_shop.demo.designer.DesignerRepository;
import hair_shop.demo.domain.Designer;
import hair_shop.demo.domain.Member;
import hair_shop.demo.domain.Menu;
import hair_shop.demo.domain.OrderTable;
import hair_shop.demo.member.MemberRepository;
import hair_shop.demo.menu.MenuRepository;
import hair_shop.demo.order.form.MonthData;
import hair_shop.demo.order.form.OrderForm;
import hair_shop.demo.order.form.Payment;
import lombok.RequiredArgsConstructor;
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
}
