package hair_shop.demo.order.form;

import hair_shop.demo.domain.OrderTable;
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


    public static List<MonthData> remakeMonthData(Map<Integer, List<OrderTable>> daySeparated) {
        List<MonthData> monthData = new ArrayList<>();
        Set<Integer> DayKey = daySeparated.keySet();

        for (Integer day : DayKey) {
            List<OrderTable> orderTables = daySeparated.get(day);
            int expectPrice = orderTables.stream().mapToInt(OrderTable::totalPrice).sum();
            int resultPrice = orderTables.stream().filter(orderTable -> {
                //todo 여기 고쳐야 될수도 있다.
                return !orderTable.getPayment().equals(Payment.NOT_PAYMENT);
            }).mapToInt(OrderTable::totalPrice).sum();
            monthData.add(MonthData.builder()
                    .day(day)
                    .expectSales(expectPrice)
                    .resultSales(resultPrice)
                    .build());
        }
        return monthData;
    }
}
