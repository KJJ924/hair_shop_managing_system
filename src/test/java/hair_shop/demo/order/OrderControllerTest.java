package hair_shop.demo.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import hair_shop.demo.designer.DesignerRepository;
import hair_shop.demo.domain.Designer;
import hair_shop.demo.domain.Member;
import hair_shop.demo.domain.Menu;
import hair_shop.demo.domain.OrderTable;
import hair_shop.demo.member.MemberRepository;
import hair_shop.demo.member.membership.MemberShipForm;
import hair_shop.demo.member.membership.MemberShipService;
import hair_shop.demo.menu.MenuRepository;
import hair_shop.demo.order.form.OrderForm;
import hair_shop.demo.order.form.Payment;
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
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
        memberShipService.getResponseToCreate(MemberShipForm.builder()
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
        mockMvc.perform(put("/order/payment")
                .param("order_id",String.valueOf(order_id))
                .param("payment","point"))
                .andExpect(status().isOk());

        OrderTable orderTable = orderRepository.findById(order_id).get();

        assertThat(orderTable.getPayment()).isEqualTo(Payment.POINT);
        assertThat(orderTable.getMember().getMemberShipPoint()).isEqualTo(9000);
        assertThat(orderTable.getMember().getLastVisitDate())
                .isBetween(LocalDateTime.now().minusMinutes(1),LocalDateTime.now());


    }

    @Test
    @DisplayName("현금 결제")
    void cashPayment()throws Exception{
        mockMvc.perform(put("/order/payment")
                .param("order_id",String.valueOf(order_id))
                .param("payment","cash"))
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

        mockMvc.perform(put("/order/payment")
                .param("order_id",String.valueOf(orderTable.getId()))
                .param("payment","point"))
                .andDo(print())
                .andExpect(status().isBadRequest());


        assertThat(orderTable.getPayment()).isEqualTo(Payment.NOT_PAYMENT);
        assertThat(orderTable.getMember().getMemberShipPoint()).isEqualTo(10000);
    }

    @Test
    @DisplayName("결제실패-중복결제")
    void payment_fail_duplicate_payment()throws Exception{
        OrderTable orderTable = orderRepository.findById(order_id).get();
        orderTable.setPayment(Payment.CASH);
        mockMvc.perform(put("/order/payment")
                .param("order_id",String.valueOf(order_id))
                .param("payment","point"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }




}