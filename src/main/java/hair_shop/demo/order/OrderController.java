package hair_shop.demo.order;

import hair_shop.demo.order.form.MonthData;
import hair_shop.demo.order.form.OrderForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping("/create/order")
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

}
