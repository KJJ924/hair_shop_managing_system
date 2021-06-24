package hair_shop.demo.modules.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import hair_shop.demo.modules.designer.domain.Designer;
import hair_shop.demo.modules.designer.repository.DesignerRepository;
import hair_shop.demo.modules.member.domain.Member;
import hair_shop.demo.modules.member.membership.dto.request.MemberShipForm;
import hair_shop.demo.modules.member.membership.service.MemberShipService;
import hair_shop.demo.modules.member.repository.MemberRepository;
import hair_shop.demo.modules.menu.domain.Menu;
import hair_shop.demo.modules.menu.repository.MenuRepository;
import hair_shop.demo.modules.order.domain.Order;
import hair_shop.demo.modules.order.domain.Payment;
import hair_shop.demo.modules.order.dto.MonthData;
import hair_shop.demo.modules.order.dto.request.RequestOrder;
import hair_shop.demo.modules.order.dto.request.RequestOrderMenuEdit;
import hair_shop.demo.modules.order.dto.request.RequestOrderTimeEdit;
import hair_shop.demo.modules.order.dto.request.RequestPayment;
import hair_shop.demo.modules.order.dto.response.ResponseOrder;
import hair_shop.demo.modules.order.repository.OrderRepository;
import hair_shop.demo.modules.order.service.OrderService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderControllerTest {

    @Autowired MockMvc mockMvc;

    @Autowired
    OrderRepository orderRepository;

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
        designerRepository.save(Designer.name("사장님"));
        menuRepository.save(Menu.builder().name("menu").price(1000).build());
        menuRepository.save(Menu.builder().name("menu2").price(100000).build());

        RequestOrder requestOrder = new RequestOrder();
        requestOrder.setDesignerName("사장님");
        requestOrder.setMenuName("menu");
        requestOrder.setMemberPhoneNumber("000");
        //"yyyy-MM-dd'T'HH:mm:ss"
        requestOrder.setReservationStart(LocalDateTime.of(9999,12,1,12,0));
        requestOrder.setReservationEnd(LocalDateTime.of(9999,12,1,12,30));
        ResponseOrder orderTable = orderService.saveOrder(requestOrder);
        order_id = orderTable.getOrderId();
    }

    @AfterEach
    void clearData(){
        memberRepository.deleteAll();
        designerRepository.deleteAll();
        menuRepository.deleteAll();
    }
    @Test
    @DisplayName("예약표 받기-성공")
    void getOrder() throws Exception{
        mockMvc.perform(get("/order/"+order_id))
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("예약표 받기-실패(해당하는 주문번호가 없을때)")
    void getOrder_fail() throws Exception{
        mockMvc.perform(get("/order/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("예약 생성-성공")
    void createOrder() throws Exception {
        RequestOrder requestOrder = new RequestOrder();
        requestOrder.setDesignerName("사장님");
        requestOrder.setMenuName("menu");
        requestOrder.setMemberPhoneNumber("000");
        //"yyyy-MM-dd'T'HH:mm:ss"
        requestOrder.setReservationStart(LocalDateTime.of(2020,12,1,12,0));
        requestOrder.setReservationEnd(LocalDateTime.of(2020,12,1,12,30));

        String content = objectMapper.writeValueAsString(requestOrder);

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("예약 생성-실패(회원이없음)")
    void createOrder_fail_noMember() throws Exception {
        RequestOrder requestOrder = new RequestOrder();
        requestOrder.setDesignerName("사장님");
        requestOrder.setMenuName("menu");
        requestOrder.setMemberPhoneNumber("111");
        //"yyyy-MM-dd'T'HH:mm:ss"
        requestOrder.setReservationStart(LocalDateTime.of(2020,12,1,12,0));
        requestOrder.setReservationEnd(LocalDateTime.of(2020,12,1,12,30));

        String content = objectMapper.writeValueAsString(requestOrder);

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("예약 생성-실패(디자이너가 없음)")
    void createOrder_fail_noDesigner() throws Exception {
        RequestOrder requestOrder = new RequestOrder();
        requestOrder.setDesignerName("알바생");
        requestOrder.setMenuName("menu");
        requestOrder.setMemberPhoneNumber("000");
        //"yyyy-MM-dd'T'HH:mm:ss"
        requestOrder.setReservationStart(LocalDateTime.of(2020,12,1,12,0));
        requestOrder.setReservationEnd(LocalDateTime.of(2020,12,1,12,30));

        String content = objectMapper.writeValueAsString(requestOrder);

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("예약 생성-실패(메뉴가 없음)")
    void createOrder_fail_noMenu() throws Exception {
        RequestOrder requestOrder = new RequestOrder();
        requestOrder.setDesignerName("사장님");
        requestOrder.setMenuName("noMenu");
        requestOrder.setMemberPhoneNumber("000");
        //"yyyy-MM-dd'T'HH:mm:ss"
        requestOrder.setReservationStart(LocalDateTime.of(2020,12,1,12,0));
        requestOrder.setReservationEnd(LocalDateTime.of(2020,12,1,12,30));

        String content = objectMapper.writeValueAsString(requestOrder);

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("포인트 결제")
    void pointPayment()throws Exception{
        RequestPayment requestPayment = RequestPayment.builder()
                .payment(Payment.POINT)
                .orderId(order_id).build();
        String content = objectMapper.writeValueAsString(requestPayment);
        mockMvc.perform(post("/order/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)).andDo(print())
                .andExpect(status().isOk());

        Order order = orderService.findByOrderId(order_id);

        assertThat(order.getPayment()).isEqualTo(Payment.POINT);
        assertThat(order.getMember().getMemberShipPoint()).isEqualTo(9000);
        assertThat(order.getMember().getLastVisitDate())
                .isBetween(LocalDate.now().minusDays(1),LocalDate.now());


    }

    @Test
    @DisplayName("현금 결제")
    void cashPayment()throws Exception{
        RequestPayment requestPayment = RequestPayment.builder()
                .payment(Payment.CASH)
                .orderId(order_id).build();
        String content = objectMapper.writeValueAsString(requestPayment);
        mockMvc.perform(post("/order/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk());

        Order order = orderService.findByOrderId(order_id);

        assertThat(order.getPayment()).isEqualTo(Payment.CASH);
        assertThat(order.getMember().getMemberShipPoint()).isEqualTo(10000);
        assertThat(order.getMember().getLastVisitDate())
                .isBetween(LocalDate.now().minusDays(1),LocalDate.now());
    }

    @Test
    @DisplayName("결제실패-포인트부족")
    void payment_fail_point_not_enough()throws Exception{

        RequestOrder requestOrder = new RequestOrder();
        requestOrder.setDesignerName("사장님");
        requestOrder.setMenuName("menu2");
        requestOrder.setMemberPhoneNumber("000");
        //"yyyy-MM-dd'T'HH:mm:ss"
        requestOrder.setReservationStart(LocalDateTime.of(9999,12,1,12,0));
        requestOrder.setReservationEnd(LocalDateTime.of(9999,12,1,12,30));
        ResponseOrder orderTable = orderService.saveOrder(requestOrder);

        RequestPayment requestPayment = RequestPayment.builder()
                .payment(Payment.POINT)
                .orderId(orderTable.getOrderId()).build();
        String content = objectMapper.writeValueAsString(requestPayment);


        mockMvc.perform(put("/order/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().is4xxClientError());

        Member member = memberRepository.findByPhone(orderTable.getMemberPhoneNumber()).orElseThrow();
        assertThat(orderTable.getPayment()).isEqualTo(Payment.NOT_PAYMENT);
        assertThat(member.getMemberShipPoint()).isEqualTo(10000);
    }

    @Test
    @DisplayName("결제실패-중복결제")
    void payment_fail_duplicate_payment()throws Exception{
        RequestPayment requestPayment = RequestPayment.builder()
                .payment(Payment.POINT)
                .orderId(order_id).build();
        String content = objectMapper.writeValueAsString(requestPayment);

        Order order = orderService.findByOrderId(order_id);
        order.setPayment(Payment.CASH);
        mockMvc.perform(put("/order/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("예약시간 변경 - 성공")
    void order_edit()throws  Exception{
        LocalDateTime startTime = LocalDateTime.now();
        RequestOrderTimeEdit editForm = RequestOrderTimeEdit.builder()
                .id(order_id)
                .reservationStart(startTime)
                .reservationEnd(startTime.plusMinutes(30)).build();

        String content = objectMapper.writeValueAsString(editForm);

        mockMvc.perform(put("/order/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk());

        Order order = orderService.findByOrderId(order_id);

        assertThat(order.getReservationStart()).isEqualTo(startTime);
        assertThat(order.getReservationEnd()).isEqualTo(startTime.plusMinutes(30));
    }

    @Test
    @DisplayName("예약시간 변경 - 실패(예약 시작 시간이 예약 종료시간보다 늦을수 없습니다)")
    void order_edit_fail_overTime()throws  Exception{
        LocalDateTime startTime = LocalDateTime.now();
         RequestOrderTimeEdit editForm = RequestOrderTimeEdit.builder()
                .id(order_id)
                .reservationStart(startTime.plusMinutes(30))
                .reservationEnd(startTime).build();

        String content = objectMapper.writeValueAsString(editForm);

        mockMvc.perform(put("/order/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("예약시간 변경 - 실패(해당하는 예약이 없음)")
    void order_edit_fail_not_found_order()throws  Exception{
        LocalDateTime startTime = LocalDateTime.now();
        RequestOrderTimeEdit editForm = RequestOrderTimeEdit.builder()
                .id(999L)
                .reservationStart(startTime)
                .reservationEnd(startTime.plusMinutes(30)).build();

        String content = objectMapper.writeValueAsString(editForm);

        mockMvc.perform(put("/order/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isNotFound());
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
        Map<LocalDate, List<ResponseOrder>> weekData = orderService.getWeekData(date, date.plusDays(2));
        String content = objectMapper.writeValueAsString(weekData);
        mockMvc.perform(get("/week")
                .param("from","2020-12-01")
                .param("to","2020-12-03"))
                .andDo(print())
                .andExpect(content().string(content))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("메뉴 변경 - 성공(추가)")
    void order_edit_menu_add()throws  Exception{
        RequestOrderMenuEdit requestOrderMenuEdit = new RequestOrderMenuEdit();
        requestOrderMenuEdit.setMenuName("menu2");
        requestOrderMenuEdit.setOrderId(order_id);

        String content = objectMapper.writeValueAsString(requestOrderMenuEdit);

        mockMvc.perform(put("/order/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk());

        Order order = orderService.findByOrderId(order_id);

        assertThat(order.menuList().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("메뉴 변경 - 성공(삭제)")
    void order_edit_menu_delete()throws Exception{
        RequestOrderMenuEdit requestOrderMenuEdit = new RequestOrderMenuEdit();
        requestOrderMenuEdit.setMenuName("menu");
        requestOrderMenuEdit.setOrderId(order_id);
        String content = objectMapper.writeValueAsString(requestOrderMenuEdit);

        mockMvc.perform(delete("/order/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk());

        Order order = orderService.findByOrderId(order_id);

        assertThat(order.menuList().size()).isZero();
    }

    @Test
    @DisplayName("예약 취소 -성공")
    void order_cancel() throws Exception{
        mockMvc.perform(delete("/order/"+order_id))
                .andExpect(status().isNoContent());

        boolean result = orderRepository.existsById(order_id);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("예약 취소 -실패(해당하는 주문이없음)")
    void order_cancel_fail() throws Exception{
        mockMvc.perform(delete("/order/4888"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("예약 취소 -실패(이미 결제가 완료됨)")
    void order_cancel_already_payment_fail() throws Exception{
        Order order = orderService.findByOrderId(order_id);
        order.setPayment(Payment.CASH);
        mockMvc.perform(delete("/order/"+order_id))
                .andExpect(status().is4xxClientError());

    }




}