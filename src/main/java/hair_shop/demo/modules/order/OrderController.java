package hair_shop.demo.modules.order;

import hair_shop.demo.Infra.apiMessage.ApiResponseMessage;
import hair_shop.demo.modules.order.domain.OrderTable;
import hair_shop.demo.modules.order.form.OrderForm;
import hair_shop.demo.modules.order.form.PaymentForm;
import hair_shop.demo.modules.order.form.edit.OrderMenuEditForm;
import hair_shop.demo.modules.order.form.edit.OrderTimeEditForm;
import hair_shop.demo.modules.order.validator.OrderEditMenuFormValidator;
import hair_shop.demo.modules.order.validator.OrderEditTimeFormValidator;
import hair_shop.demo.modules.order.validator.OrderFromValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class OrderController {
    public static final String NOT_FOUND_ORDER ="해당하는 예약이 없음";

    private final OrderService orderService;


    private final OrderFromValidator orderFromValidator;
    private final OrderEditTimeFormValidator orderEditTimeFormValidator;
    private final OrderEditMenuFormValidator orderEditMenuFormValidator;
    private final OrderRepository orderRepository;

    @InitBinder("orderForm")
    public void orderFormValid(WebDataBinder webDataBinder){
        webDataBinder.addValidators(orderFromValidator);
    }
    @InitBinder("orderTimeEditForm")
    public void orderEditTimeValid(WebDataBinder webDataBinder){
        webDataBinder.addValidators(orderEditTimeFormValidator);
    }
    @InitBinder("orderMenuEditForm")
    public void orderEditMenuValid(WebDataBinder webDataBinder){
        webDataBinder.addValidators(orderEditMenuFormValidator);
    }


    @PostMapping("/order")
    public ResponseEntity<Object> createOrder(@RequestBody @Valid OrderForm orderForm , Errors error){
        if(error.hasErrors()){
            return ApiResponseMessage.error(error);
        }
        OrderTable order = orderService.saveOrder(orderForm);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<Object> getOrder(@PathVariable("id") Optional<OrderTable> orderTable , @PathVariable String id){
        if(orderTable.isEmpty()){
            return ApiResponseMessage.error(id,NOT_FOUND_ORDER);
        }
        return ResponseEntity.ok(orderTable.get());
    }

    @GetMapping("/month")
    public ResponseEntity<Object> getMonthData(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from){
        return ResponseEntity.ok(orderService.getMonthData(from,from.plusMonths(1)));
    }

    @GetMapping("/week")
    public ResponseEntity<Object> getWeekData(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to){
        return ResponseEntity.ok(orderService.getWeekData(from, to));
    }

    @PutMapping("/order/payment")
    public ResponseEntity<Object> payment(@RequestBody PaymentForm paymentForm){
        return orderService.payment(paymentForm);
    }

    @PutMapping("/order/time")
    public ResponseEntity<Object> orderTimeEdit(@RequestBody @Valid OrderTimeEditForm orderTimeEditForm, Errors errors){
        if(errors.hasErrors()){
            return ApiResponseMessage.error(errors);
        }
        orderService.editTime(orderTimeEditForm);
        return ApiResponseMessage.success("성공적으로 변경되었습니다");
    }

    // todo  여기서 action 은  Add or Delete
    @PutMapping("/order/menu/{action}")
    public ResponseEntity<Object> orderMenuEdit(@RequestBody @Valid OrderMenuEditForm orderMenuEditForm, Errors errors
            ,@PathVariable String action){
        if(!action.equals("add") && !action.equals("delete")){
            return ApiResponseMessage.error(action,"요청 PathVariable 을 다시 확인해주세요");
        }
        if(errors.hasErrors()){
            return ApiResponseMessage.error(errors);
        }
        orderService.editMenu(orderMenuEditForm,action);
        return ApiResponseMessage.success("성공적으로 변경되었습니다");
    }

    @DeleteMapping("/order/{id}")
    public ResponseEntity<Object> testDelete(@PathVariable("id") Optional<OrderTable> orderTable
            ,@PathVariable Long id){
        if(orderTable.isEmpty()){
            return ApiResponseMessage.error(id.toString(),NOT_FOUND_ORDER);
        }
        if(orderTable.get().checkPayment()){
            return ApiResponseMessage.error(id.toString(),"이미 결제한 예약은 삭제가 불가능합니다");
        }
        orderRepository.delete(orderTable.get());
        return ApiResponseMessage.success("성공적으로 삭제되었습니다.");
    }

}
