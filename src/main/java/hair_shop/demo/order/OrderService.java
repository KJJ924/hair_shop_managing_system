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

    public void saveOrder(OrderForm orderForm) {
        OrderTable order = makeOrder(orderForm);
        orderRepository.save(order);
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
        Map<Integer, List<OrderTable>> daySeparated = daySeparated(orderList);
        return remakeMonthData(daySeparated);
    }


    private List<MonthData> remakeMonthData(Map<Integer, List<OrderTable>> daySeparated) {
        List<MonthData> monthData = new ArrayList<>();
        Set<Integer> DayKey = daySeparated.keySet();

        for (Integer day : DayKey) {
            List<OrderTable> orderTables = daySeparated.get(day);
            int expectPrice = orderTables.stream().mapToInt(OrderTable::totalPrice).sum();
            int resultPrice = orderTables.stream().filter(OrderTable::isStatus).mapToInt(OrderTable::totalPrice).sum();
            monthData.add(MonthData.builder()
                    .day(day)
                    .expectSales(expectPrice)
                    .resultSales(resultPrice)
                    .build());
        }
        return monthData;
    }

    private Map<Integer, List<OrderTable>>  daySeparated(List<OrderTable> data) {
        Map<Integer, List<OrderTable>> tableMap = new LinkedHashMap<>();

        data.forEach(orderTable -> {
            int dayOfMonth = orderTable.getReservationStart().getDayOfMonth();
            List<OrderTable> orderList = tableMap.get(dayOfMonth);
            if(orderList ==null){
                tableMap.put(dayOfMonth,new ArrayList<>(Collections.singletonList(orderTable)));
            }else {
                orderList.add(orderTable);
            }
        });
        return tableMap;
    }

    public Map<Integer, List<OrderTable>> getWeekData(LocalDateTime standardDay, LocalDateTime plusDay) {
        List<OrderTable> orderList = orderRepository.findByReservationStartBetween(standardDay, plusDay);
        return daySeparated(orderList);

    }
}
