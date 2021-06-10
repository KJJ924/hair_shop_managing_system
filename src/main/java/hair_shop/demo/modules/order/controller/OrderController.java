package hair_shop.demo.modules.order.controller;

import hair_shop.demo.modules.order.dto.request.OrderForm;
import hair_shop.demo.modules.order.dto.request.RequestOrderMenuEdit;
import hair_shop.demo.modules.order.dto.request.PaymentForm;
import hair_shop.demo.modules.order.dto.request.RequestOrderTimeEdit;
import hair_shop.demo.modules.order.dto.response.ResponseOrder;
import hair_shop.demo.modules.order.service.OrderService;
import java.time.LocalDate;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<ResponseOrder> createOrder(@RequestBody @Valid OrderForm orderForm) {
        ResponseOrder order = orderService.saveOrder(orderForm);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<ResponseOrder> getOrder(@PathVariable("id") Long orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @PutMapping("/order/reservation")
    public ResponseEntity<ResponseOrder> orderTimeEdit(
        @RequestBody @Valid RequestOrderTimeEdit form) {
        return ResponseEntity.ok(orderService.editTime(form));
    }

    @GetMapping("/month")
    public ResponseEntity<Object> getMonthData(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from) {
        return ResponseEntity.ok(orderService.getMonthData(from, from.plusMonths(1)));
    }

    @GetMapping("/week")
    public ResponseEntity<Object> getWeekData(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(orderService.getWeekData(from, to));
    }

    @PutMapping("/order/payment")
    public ResponseEntity<Object> payment(@RequestBody PaymentForm paymentForm) {
        return orderService.payment(paymentForm);
    }

    @PutMapping("/order/menu")
    public ResponseEntity<ResponseOrder> orderMenuAdd(@RequestBody @Valid RequestOrderMenuEdit form) {
        return ResponseEntity.ok(orderService.addMenu(form));
    }

    @DeleteMapping("/order/menu")
    public ResponseEntity<ResponseOrder> orderMenuDelete(@RequestBody @Valid RequestOrderMenuEdit form) {
        return ResponseEntity.ok(orderService.deleteMenu(form));
    }

    @DeleteMapping("/order/{id}")
    public ResponseEntity<Object> testDelete(@PathVariable("id") Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
