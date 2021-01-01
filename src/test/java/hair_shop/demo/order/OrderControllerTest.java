package hair_shop.demo.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import hair_shop.demo.designer.DesignerRepository;
import hair_shop.demo.domain.Designer;
import hair_shop.demo.domain.Member;
import hair_shop.demo.domain.Menu;
import hair_shop.demo.domain.OrderTable;
import hair_shop.demo.member.MemberRepository;
import hair_shop.demo.member.membership.form.MemberShipForm;
import hair_shop.demo.member.membership.MemberShipService;
import hair_shop.demo.menu.MenuRepository;
import hair_shop.demo.order.form.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderControllerTest {

    @Autowired MockMvc mockMvc;

    @Autowired OrderRepository orderRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    DesignerRepository designerRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    OrderService orderService;

    @Autowired
    MemberShipService memberShipService;

    Long order_id;

    @BeforeEach
    void initData(){
        memberRepository.save(Member.builder().name("재준").phone("000").build());
        memberShipService.createMemberShip(MemberShipForm.builder()
                .phone("000")
                .point(10000)
                .build());
        designerRepository.save(Designer.builder().name("사장님").build());
        menuRepository.save(Menu.builder().name("menu").price(1000).build());
        menuRepository.save(Menu.builder().name("menu2").price(100000).build());

        OrderForm orderForm = new OrderForm();
        orderForm.setDesignerName("사장님");
        orderForm.setMenuName("menu");
        orderForm.setMemberPhoneNumber("000");
        //"yyyy-MM-dd'T'HH:mm:ss"
        orderForm.setReservationStart(LocalDateTime.of(9999,12,1,12,0));
        orderForm.setReservationEnd(LocalDateTime.of(9999,12,1,12,30));
        OrderTable orderTable = orderService.saveOrder(orderForm);
        order_id = orderTable.getId();
    }

    @AfterEach
    void clearData(){
        memberRepository.deleteAll();
        designerRepository.deleteAll();
        menuRepository.deleteAll();
    }

    @Test
    @DisplayName("예약 생성-성공")
    void createOrder() throws Exception {
        OrderForm orderForm = new OrderForm();
        orderForm.setDesignerName("사장님");
        orderForm.setMenuName("menu");
        orderForm.setMemberPhoneNumber("000");
        //"yyyy-MM-dd'T'HH:mm:ss"
        orderForm.setReservationStart(LocalDateTime.of(2020,12,1,12,0));
        orderForm.setReservationEnd(LocalDateTime.of(2020,12,1,12,30));

        String content = objectMapper.writeValueAsString(orderForm);

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("예약 생성-실패(회원이없음)")
    void createOrder_fail_noMember() throws Exception {
        OrderForm orderForm = new OrderForm();
        orderForm.setDesignerName("사장님");
        orderForm.setMenuName("menu");
        orderForm.setMemberPhoneNumber("111");
        //"yyyy-MM-dd'T'HH:mm:ss"
        orderForm.setReservationStart(LocalDateTime.of(2020,12,1,12,0));
        orderForm.setReservationEnd(LocalDateTime.of(2020,12,1,12,30));

        String content = objectMapper.writeValueAsString(orderForm);

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("예약 생성-실패(디자이너가 없음)")
    void createOrder_fail_noDesigner() throws Exception {
        OrderForm orderForm = new OrderForm();
        orderForm.setDesignerName("알바생");
        orderForm.setMenuName("menu");
        orderForm.setMemberPhoneNumber("000");
        //"yyyy-MM-dd'T'HH:mm:ss"
        orderForm.setReservationStart(LocalDateTime.of(2020,12,1,12,0));
        orderForm.setReservationEnd(LocalDateTime.of(2020,12,1,12,30));

        String content = objectMapper.writeValueAsString(orderForm);

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("예약 생성-실패(메뉴가 없음)")
    void createOrder_fail_noMenu() throws Exception {
        OrderForm orderForm = new OrderForm();
        orderForm.setDesignerName("사장님");
        orderForm.setMenuName("noMenu");
        orderForm.setMemberPhoneNumber("000");
        //"yyyy-MM-dd'T'HH:mm:ss"
        orderForm.setReservationStart(LocalDateTime.of(2020,12,1,12,0));
        orderForm.setReservationEnd(LocalDateTime.of(2020,12,1,12,30));

        String content = objectMapper.writeValueAsString(orderForm);

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("포인트 결제")
    void pointPayment()throws Exception{
        PaymentForm paymentForm = PaymentForm.builder()
                .payment(Payment.POINT)
                .order_id(order_id).build();
        String content = objectMapper.writeValueAsString(paymentForm);
        mockMvc.perform(put("/order/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk());

        OrderTable orderTable = orderRepository.findById(order_id).get();

        assertThat(orderTable.getPayment()).isEqualTo(Payment.POINT);
        assertThat(orderTable.getMember().getMemberShipPoint()).isEqualTo(9000);
        assertThat(orderTable.getMember().getLastVisitDate())
                .isBetween(LocalDateTime.now().minusMinutes(1),LocalDateTime.now());


    }

    @Test
    @DisplayName("포인트+현금 결제")
    void pointAndCashPayment()throws Exception{
        PaymentForm paymentForm = PaymentForm.builder()
                .payment(Payment.CASH_AND_POINT)
                .cash(500)
                .order_id(order_id).build();

        String content = objectMapper.writeValueAsString(paymentForm);
        mockMvc.perform(put("/order/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk());

        OrderTable orderTable = orderRepository.findById(order_id).get();

        assertThat(orderTable.getPayment()).isEqualTo(Payment.CASH_AND_POINT);
        assertThat(orderTable.getMember().getMemberShipPoint()).isEqualTo(9500);
        assertThat(orderTable.getMember().getLastVisitDate())
                .isBetween(LocalDateTime.now().minusMinutes(1),LocalDateTime.now());
    }

    @Test
    @DisplayName("포인트+현금 결제- 실패(포인트가 부족한 경우)")
    void pointAndCashPayment_fail()throws Exception{

        OrderForm orderForm = new OrderForm();
        orderForm.setDesignerName("사장님");
        orderForm.setMenuName("menu2");
        orderForm.setMemberPhoneNumber("000");
        //"yyyy-MM-dd'T'HH:mm:ss"
        orderForm.setReservationStart(LocalDateTime.of(9999,12,1,12,0));
        orderForm.setReservationEnd(LocalDateTime.of(9999,12,1,12,30));
        OrderTable orderTable = orderService.saveOrder(orderForm);

        PaymentForm paymentForm = PaymentForm.builder()
                .payment(Payment.CASH_AND_POINT)
                .cash(500)
                .order_id(orderTable.getId()).build();

        String content = objectMapper.writeValueAsString(paymentForm);
        mockMvc.perform(put("/order/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("현금 결제")
    void cashPayment()throws Exception{
        PaymentForm paymentForm = PaymentForm.builder()
                .payment(Payment.CASH)
                .order_id(order_id).build();
        String content = objectMapper.writeValueAsString(paymentForm);
        mockMvc.perform(put("/order/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk());

        OrderTable orderTable = orderRepository.findById(order_id).get();

        assertThat(orderTable.getPayment()).isEqualTo(Payment.CASH);
        assertThat(orderTable.getMember().getMemberShipPoint()).isEqualTo(10000);
        assertThat(orderTable.getMember().getLastVisitDate())
                .isBetween(LocalDateTime.now().minusMinutes(1),LocalDateTime.now());
    }

    @Test
    @DisplayName("결제실패-포인트부족")
    void payment_fail_point_not_enough()throws Exception{

        OrderForm orderForm = new OrderForm();
        orderForm.setDesignerName("사장님");
        orderForm.setMenuName("menu2");
        orderForm.setMemberPhoneNumber("000");
        //"yyyy-MM-dd'T'HH:mm:ss"
        orderForm.setReservationStart(LocalDateTime.of(9999,12,1,12,0));
        orderForm.setReservationEnd(LocalDateTime.of(9999,12,1,12,30));
        OrderTable orderTable = orderService.saveOrder(orderForm);

        PaymentForm paymentForm = PaymentForm.builder()
                .payment(Payment.POINT)
                .order_id(orderTable.getId()).build();
        String content = objectMapper.writeValueAsString(paymentForm);


        mockMvc.perform(put("/order/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest());


        assertThat(orderTable.getPayment()).isEqualTo(Payment.NOT_PAYMENT);
        assertThat(orderTable.getMember().getMemberShipPoint()).isEqualTo(10000);
    }

    @Test
    @DisplayName("결제실패-중복결제")
    void payment_fail_duplicate_payment()throws Exception{
        PaymentForm paymentForm = PaymentForm.builder()
                .payment(Payment.POINT)
                .order_id(order_id).build();
        String content = objectMapper.writeValueAsString(paymentForm);

        OrderTable orderTable = orderRepository.findById(order_id).get();
        orderTable.setPayment(Payment.CASH);
        mockMvc.perform(put("/order/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("예약시간 변경 - 성공")
    void order_edit()throws  Exception{
        LocalDateTime startTime = LocalDateTime.now();
        OrderEditForm editForm = OrderEditForm.builder()
                .id(order_id)
                .reservationStart(startTime)
                .reservationEnd(startTime.plusMinutes(30)).build();

        String content = objectMapper.writeValueAsString(editForm);

        mockMvc.perform(put("/order/time")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk());

        OrderTable orderTable = orderRepository.findById(order_id).get();

        assertThat(orderTable.getReservationStart()).isEqualTo(startTime);
        assertThat(orderTable.getReservationEnd()).isEqualTo(startTime.plusMinutes(30));
    }

    @Test
    @DisplayName("예약시간 변경 - 실패(예약 시작 시간이 예약 종료시간보다 늦을수 없습니다)")
    void order_edit_fail_overTime()throws  Exception{
        LocalDateTime startTime = LocalDateTime.now();
        OrderEditForm editForm = OrderEditForm.builder()
                .id(order_id)
                .reservationStart(startTime.plusMinutes(30))
                .reservationEnd(startTime).build();

        String content = objectMapper.writeValueAsString(editForm);

        mockMvc.perform(put("/order/time")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("예약시간 변경 - 실패(해당하는 예약이 없음)")
    void order_edit_fail_not_found_order()throws  Exception{
        LocalDateTime startTime = LocalDateTime.now();
        OrderEditForm editForm = OrderEditForm.builder()
                .id(999L)
                .reservationStart(startTime)
                .reservationEnd(startTime.plusMinutes(30)).build();

        String content = objectMapper.writeValueAsString(editForm);

        mockMvc.perform(put("/order/time")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("선택 월 일별 매출 현황")
    void order_month_data()throws  Exception{
        LocalDate date = LocalDate.of(2020, 12, 1);
        List<MonthData> monthData = orderService.getMonthData(date, date.plusMonths(1));
        String content = objectMapper.writeValueAsString(monthData);
        mockMvc.perform(get("/month")
                .param("from","2020-12-01"))
                .andDo(print())
                .andExpect(content().string(content))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName(" 선택 일 범위 만큼의 주문 현황 받기")
    void order_week_data()throws  Exception{
        LocalDate date = LocalDate.of(2020, 12, 1);
        Map<Integer, List<OrderTable>> weekData = orderService.getWeekData(date, date.plusDays(2));
        String content = objectMapper.writeValueAsString(weekData);
        mockMvc.perform(get("/week")
                .param("from","2020-12-01")
                .param("to","2020-12-03"))
                .andDo(print())
                .andExpect(content().string(content))
                .andExpect(status().isOk());
    }




}