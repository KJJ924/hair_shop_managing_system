package hair_shop.demo.modules.order.dto;

import hair_shop.demo.modules.order.domain.Order;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Builder
public class MonthData {

    private int day;
    private int expectSales;
    private int resultSales;


    public static List<MonthData> remakeMonthData(Map<Integer, List<Order>> daySeparated) {
        List<MonthData> monthData = new ArrayList<>();
        Set<Integer> dayKey = daySeparated.keySet();

        for (Integer day : dayKey) {
            List<Order> orders = daySeparated.get(day);
            int expectPrice = orders.stream().mapToInt(Order::totalPrice).sum();
            int resultPrice = orders.stream().filter(Order::checkPayment).mapToInt(Order::totalPrice).sum();
            monthData.add(MonthData.builder()
                    .day(day)
                    .expectSales(expectPrice)
                    .resultSales(resultPrice)
                    .build());
        }
        return monthData;
    }
}
