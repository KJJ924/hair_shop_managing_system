package hair_shop.demo.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hair_shop.demo.designer.DesignerRepository;
import hair_shop.demo.domain.Designer;
import hair_shop.demo.domain.Member;
import hair_shop.demo.domain.Menu;
import hair_shop.demo.member.MemberRepository;
import hair_shop.demo.menu.MenuRepository;
import hair_shop.demo.order.form.OrderForm;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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



    @BeforeEach
    void initData(){
        memberRepository.save(Member.builder().name("재준").phone("000").build());
        designerRepository.save(Designer.builder().name("사장님").build());
        menuRepository.save(Menu.builder().name("menu").price(1000).build());
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




}