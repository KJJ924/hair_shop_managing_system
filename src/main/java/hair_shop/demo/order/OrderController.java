package hair_shop.demo.order;

import hair_shop.demo.domain.OrderTable;
import hair_shop.demo.order.form.MonthData;
import hair_shop.demo.order.form.OrderForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping("/order")
    @ResponseBody
    public ResponseEntity createOrder(@RequestBody OrderForm orderForm){

        orderService.saveOrder(orderForm);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/month/{year}/{month}")
    @ResponseBody
    public List<MonthData> getMonthData(@PathVariable int year, @PathVariable int month){
        LocalDateTime standardMonth = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime plusMonth = standardMonth.plusMonths(1);

        return orderService.getMonthData(standardMonth,plusMonth);
    }

    @GetMapping("/week/{year}/{month}/{baseDate}/{targetDay}")
    public Map<Integer, List<OrderTable>> getWeekData(@PathVariable int year,@PathVariable int month
            , @PathVariable int baseDate, @PathVariable int targetDay){
        LocalDateTime standardDay = LocalDateTime.of(year,month,baseDate,0,0);
        LocalDateTime plusDay = standardDay.plusDays(targetDay-baseDate).plusHours(24);
        Map<Integer, List<OrderTable>> weekData = orderService.getWeekData(standardDay, plusDay);
        return weekData;
    }


}
