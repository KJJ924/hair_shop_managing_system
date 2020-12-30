package hair_shop.demo.order;

import hair_shop.apiMessage.ApiResponseMessage;
import hair_shop.demo.domain.OrderTable;
import hair_shop.demo.order.form.OrderEditForm;
import hair_shop.demo.order.form.OrderForm;
import hair_shop.demo.order.form.PaymentForm;
import hair_shop.demo.order.validator.OrderEditFromValidator;
import hair_shop.demo.order.validator.OrderFromValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class OrderController {
    public static final String NOT_FOUND_ORDER ="해당하는 예약이 없음";

    private final OrderService orderService;


    private final OrderFromValidator orderFromValidator;
    private final OrderEditFromValidator orderEditFromValidator;

    @InitBinder("orderForm")
    public void orderFormValid(WebDataBinder webDataBinder){
        webDataBinder.addValidators(orderFromValidator);
    }
    @InitBinder("orderEditForm")
    public void orderEditFormValid(WebDataBinder webDataBinder){
        webDataBinder.addValidators(orderEditFromValidator);
    }

    @PostMapping("/order")
    public ResponseEntity<Object> createOrder(@RequestBody @Valid OrderForm orderForm , Errors error){
        if(error.hasErrors()){
            return ApiResponseMessage.error(error);
        }
        OrderTable order = orderService.saveOrder(orderForm);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/month")
    public ResponseEntity<Object> getMonthData(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from){
        return ResponseEntity.ok(orderService.getMonthData(from,from.plusMonths(1)));
    }

    @GetMapping("/week")
    public ResponseEntity<Object> getWeekData(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
            ){
        return ResponseEntity.ok(orderService.getWeekData(from, to));
    }

    @PutMapping("/order/payment")
    public ResponseEntity<Object> payment(@RequestBody PaymentForm paymentForm){
        return orderService.payment(paymentForm);
    }

    @PutMapping("/order/edit")
    public ResponseEntity<Object> orderEdit(@RequestBody @Valid OrderEditForm orderEditForm, Errors errors){
        if(errors.hasErrors()){
            return ApiResponseMessage.error(errors);
        }
        orderService.editOrder(orderEditForm);
        return ApiResponseMessage.success("성공적으로 변경되었습니다");
    }
}
