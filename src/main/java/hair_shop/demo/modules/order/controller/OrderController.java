package hair_shop.demo.modules.order.controller;

import hair_shop.demo.Infra.apiMessage.ApiResponseMessage;
import hair_shop.demo.modules.order.domain.OrderTable;
import hair_shop.demo.modules.order.dto.request.OrderForm;
import hair_shop.demo.modules.order.dto.request.OrderMenuEditForm;
import hair_shop.demo.modules.order.dto.request.OrderTimeEditForm;
import hair_shop.demo.modules.order.dto.request.PaymentForm;
import hair_shop.demo.modules.order.dto.response.ResponseOrder;
import hair_shop.demo.modules.order.repository.OrderRepository;
import hair_shop.demo.modules.order.service.OrderService;
import hair_shop.demo.modules.order.validator.OrderEditMenuFormValidator;
import hair_shop.demo.modules.order.validator.OrderEditTimeFormValidator;
import java.time.LocalDate;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {
    public static final String NOT_FOUND_ORDER ="해당하는 예약이 없음";

    private final OrderService orderService;
    private final OrderEditTimeFormValidator orderEditTimeFormValidator;
    private final OrderEditMenuFormValidator orderEditMenuFormValidator;
    private final OrderRepository orderRepository;

    @InitBinder("orderTimeEditForm")
    public void orderEditTimeValid(WebDataBinder webDataBinder){
        webDataBinder.addValidators(orderEditTimeFormValidator);
    }
    @InitBinder("orderMenuEditForm")
    public void orderEditMenuValid(WebDataBinder webDataBinder){
        webDataBinder.addValidators(orderEditMenuFormValidator);
    }


    @PostMapping("/order")
    public ResponseEntity<ResponseOrder> createOrder(@RequestBody @Valid OrderForm orderForm){
        ResponseOrder order = orderService.saveOrder(orderForm);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<ResponseOrder> getOrder(@PathVariable("id") Long orderId){
        return ResponseEntity.ok(orderService.getOrder(orderId));
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
