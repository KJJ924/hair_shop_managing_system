package hair_shop.demo.order;

import hair_shop.apiMessage.ApiResponseMessage;
import hair_shop.demo.designer.DesignerController;
import hair_shop.demo.designer.DesignerRepository;
import hair_shop.demo.domain.OrderTable;
import hair_shop.demo.member.MemberController;
import hair_shop.demo.member.MemberRepository;
import hair_shop.demo.menu.MenuController;
import hair_shop.demo.menu.MenuRepository;
import hair_shop.demo.order.form.OrderForm;
import hair_shop.demo.order.form.Payment;

import hair_shop.demo.order.form.PaymentForm;
import hair_shop.demo.order.validator.OrderFromValidator;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class OrderController {
    public static final String NOT_FOUND_ORDER ="해당하는 예약이 없음";

    private final OrderService orderService;


    private final OrderFromValidator orderFromValidator;

    @InitBinder("orderForm")
    public void orderFormValid(WebDataBinder webDataBinder){
        webDataBinder.addValidators(orderFromValidator);
    }

    @PostMapping("/order")
    public ResponseEntity<Object> createOrder(@RequestBody @Valid OrderForm orderForm , Errors error){
        if(error.hasErrors()){
            return ApiResponseMessage.error(error);
        }
        OrderTable order = orderService.saveOrder(orderForm);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/month/{year}/{month}")
    public ResponseEntity<Object> getMonthData(@PathVariable int year, @PathVariable int month){
        LocalDateTime standardMonth = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime plusMonth = standardMonth.plusMonths(1);
        return ResponseEntity.ok(orderService.getMonthData(standardMonth,plusMonth));
    }

    @GetMapping("/week/{year}/{month}/{baseDate}/{targetDay}")
    public ResponseEntity<Object> getWeekData(@PathVariable int year,@PathVariable int month
            , @PathVariable int baseDate, @PathVariable int targetDay){
        LocalDateTime standardDay = LocalDateTime.of(year,month,baseDate,0,0);
        LocalDateTime plusDay = standardDay.plusDays(targetDay-baseDate).plusHours(24);
        return ResponseEntity.ok(orderService.getWeekData(standardDay, plusDay));
    }

    @PutMapping("/order/payment")
    public ResponseEntity<Object> payment(@RequestBody PaymentForm paymentForm){
        return orderService.payment(paymentForm);
    }
}
