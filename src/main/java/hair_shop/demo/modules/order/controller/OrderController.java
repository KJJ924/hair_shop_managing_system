package hair_shop.demo.modules.order.controller;

import hair_shop.demo.modules.order.dto.request.RequestOrder;
import hair_shop.demo.modules.order.dto.request.RequestOrderMenuEdit;
import hair_shop.demo.modules.order.dto.request.RequestOrderTimeEdit;
import hair_shop.demo.modules.order.dto.request.RequestPayment;
import hair_shop.demo.modules.order.dto.response.ResponseOrder;
import hair_shop.demo.modules.order.payment.dto.response.ResponsePayment;
import hair_shop.demo.modules.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = {"orderController"})
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    @ApiOperation(value="예약 추가", notes="예약 일정을 생성합니다.")
    public ResponseEntity<ResponseOrder> createOrder(@RequestBody @Valid RequestOrder requestOrder) {
        ResponseOrder order = orderService.saveOrder(requestOrder);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/order/{id}")
    @ApiOperation(value="등록된 예약 가져오기", notes="등록된 번호로 예약을 가져옵니다.")
    public ResponseEntity<ResponseOrder> getOrder(@PathVariable("id") Long orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @PutMapping("/order/reservation")
    @ApiOperation(value="예약시간 수정하기", notes="예약 시간을 수정합니다.")
    public ResponseEntity<ResponseOrder> orderTimeEdit(
        @RequestBody @Valid RequestOrderTimeEdit form) {
        return ResponseEntity.ok(orderService.editTime(form));
    }

    @GetMapping("/month")
    @ApiOperation(value="기준달로부터 한달 매출통계보기", notes="일별 매출액(예상금액,실제매출)을 출력합니다.")
    public ResponseEntity<Object> getMonthData(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from) {
        return ResponseEntity.ok(orderService.getMonthData(from, from.plusMonths(1)));
    }

    @GetMapping("/week")
    @ApiOperation(value="선택한 기간 만큼의 예약보기", notes="선택한 기간만큼의 일별 예약 현황을 출력합니다.")
    public ResponseEntity<Object> getWeekData(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(orderService.getWeekData(from, to));
    }

    @PutMapping("/order/payment")
    @ApiOperation(value="예약 결제하기", notes="결제 타입별로 예약을 결제합니다(CASH,POINT)")
    public ResponseEntity<ResponsePayment> payment(@RequestBody RequestPayment requestPayment) {
        return ResponseEntity.ok(orderService.payment(requestPayment));
    }

    @PutMapping("/order/menu")
    @ApiOperation(value="예약에 메뉴 추가", notes="기존 예약에 메뉴를 추가합니다(중복 메뉴 허용하지않음).")
    public ResponseEntity<ResponseOrder> orderMenuAdd(@RequestBody @Valid RequestOrderMenuEdit form) {
        return ResponseEntity.ok(orderService.addMenu(form));
    }

    @DeleteMapping("/order/menu")
    @ApiOperation(value="예약에 메뉴 삭제", notes="기존 예약에 메뉴를 삭제합니다")
    public ResponseEntity<ResponseOrder> orderMenuDelete(@RequestBody @Valid RequestOrderMenuEdit form) {
        return ResponseEntity.ok(orderService.deleteMenu(form));
    }

    @DeleteMapping("/order/{id}")
    @ApiOperation(value="예약 취소", notes="예약을 취소합니다.")
    public ResponseEntity<Object> testDelete(@PathVariable("id") Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
