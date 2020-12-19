package hair_shop.demo.order.form;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MonthData {

    private int day;
    private int expectSales;
    private int resultSales;
}
