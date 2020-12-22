package hair_shop.demo.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hair_shop.apiMessage.ApiResponseMessage;
import hair_shop.demo.designer.DesignerRepository;
import hair_shop.demo.domain.OrderTable;
import hair_shop.demo.member.MemberController;
import hair_shop.demo.member.MemberRepository;
import hair_shop.demo.menu.MenuController;
import hair_shop.demo.menu.MenuRepository;
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
    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;
    private final MenuRepository menuRepository;
    private final DesignerRepository designerRepository;

    @PostMapping("/order")
    @ResponseBody
    public ResponseEntity<Object> createOrder(@RequestBody OrderForm orderForm){
        if(!memberRepository.existsByPhone(orderForm.getMemberPhoneNumber())){
            return ApiResponseMessage.createError(orderForm.getMemberPhoneNumber(), MemberController.NOT_FOUND_MEMBER);
        }
        if(!menuRepository.existsByName(orderForm.getMenuName())){
            return ApiResponseMessage.createError(orderForm.getMenuName(), MenuController.NOT_FOUND_MENU);
        }
        if(!designerRepository.existsByName(orderForm.getDesignerName())){
            return ApiResponseMessage.createError(orderForm.getDesignerName(),"해당 디자이너가 존재하지않음");
        }
        OrderTable order = orderService.saveOrder(orderForm);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/month/{year}/{month}")
    @ResponseBody
    public ResponseEntity<Object> getMonthData(@PathVariable int year, @PathVariable int month){
        LocalDateTime standardMonth = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime plusMonth = standardMonth.plusMonths(1);
        return ResponseEntity.ok(orderService.getMonthData(standardMonth,plusMonth));
    }

    @GetMapping("/week/{year}/{month}/{baseDate}/{targetDay}")
    public ResponseEntity<Object> getWeekData(@PathVariable int year,@PathVariable int month
            , @PathVariable int baseDate, @PathVariable int targetDay) throws JsonProcessingException {
        LocalDateTime standardDay = LocalDateTime.of(year,month,baseDate,0,0);
        LocalDateTime plusDay = standardDay.plusDays(targetDay-baseDate).plusHours(24);
        Map<Integer, List<OrderTable>> weekData = orderService.getWeekData(standardDay, plusDay);
        String content = objectMapper.writeValueAsString(weekData);
        return ResponseEntity.ok(content);
    }



}
