package hair_shop.demo.order;

import hair_shop.demo.order.form.OrderForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.swing.text.html.parser.Entity;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderService orderService;


    @PostMapping("/create/order")
    @ResponseBody
    public ResponseEntity createOrder(@RequestBody OrderForm orderForm){

        orderService.saveOrder(orderForm);
        return ResponseEntity.ok().build();
    }
}
