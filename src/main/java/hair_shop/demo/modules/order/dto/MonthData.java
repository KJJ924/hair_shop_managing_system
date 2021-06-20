package hair_shop.demo.modules.order.dto;

import hair_shop.demo.modules.order.domain.Payment;
import hair_shop.demo.modules.order.dto.response.ResponseOrder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MonthData {

    private LocalDate day;
    private int expectSales;
    private int sales;


    private MonthData(LocalDate day, int expectSales, int sales) {
        this.day = day;
        this.expectSales = expectSales;
        this.sales = sales;
    }

    public static List<MonthData> remakeMonthData(Map<LocalDate, List<ResponseOrder>> data) {
        List<MonthData> monthData = new ArrayList<>();
        Set<LocalDate> dayKey = data.keySet();

        for (LocalDate day : dayKey) {
            List<ResponseOrder> orders = data.get(day);

            int expectSales = orders.stream()
                .mapToInt(ResponseOrder::getPrice)
                .sum();

            int sales = orders.stream()
                .filter(o -> !o.getPayment().equals(Payment.NOT_PAYMENT))
                .mapToInt(ResponseOrder::getPrice)
                .sum();

            monthData.add(new MonthData(day, expectSales, sales));
        }
        return monthData;
    }
}
