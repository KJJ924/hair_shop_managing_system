package hair_shop.demo.modules.member.membership;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import hair_shop.demo.modules.member.repository.MemberRepository;
import hair_shop.demo.modules.member.service.MemberService;
import hair_shop.demo.modules.member.domain.Member;
import hair_shop.demo.modules.member.dto.request.RequestMemberForm;
import hair_shop.demo.modules.member.membership.dto.request.MemberShipForm;
import hair_shop.demo.modules.member.membership.service.MemberShipService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberShipControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberShipService memberShipService;

    @BeforeEach
    void initData() {
        RequestMemberForm requestMemberForm = new RequestMemberForm();
        requestMemberForm.setName("test");
        requestMemberForm.setPhone("010");
        memberService.saveMember(requestMemberForm);
    }

    @AfterEach
    void clearData() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원권 생성- 성공")
    void createMemberShip() throws Exception {
        MemberShipForm form = MemberShipForm.builder().phone("010").point(10000).build();
        String content = objectMapper.writeValueAsString(form);

        mockMvc.perform(post("/membership")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andDo(print())
            .andExpect(status().isOk());

        Member member = memberService.findByPhone(form.getPhone());
        assertThat(member.getMemberShip()).isNotNull();
        assertThat(member.getMemberShip().getPoint()).isEqualTo(10000);
        assertThat(member.getMemberShip().getMemberPhone()).isEqualTo("010");
    }

    @Test
    @DisplayName("회원권 생성-실패(해당 맴버가없음)")
    void createMemberShip_fail_not_found_member() throws Exception {
        MemberShipForm form = MemberShipForm.builder().phone("011").point(10000).build();
        String content = objectMapper.writeValueAsString(form);

        mockMvc.perform(post("/membership")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("회원권 생성-실패(회원권이 이미존재함)")
    void createMemberShip_fail_already_memberShip() throws Exception {

        MemberShipForm form = MemberShipForm.builder().phone("010").point(10000).build();
        memberShipService.createMemberShip(form);

        String content = objectMapper.writeValueAsString(form);
        mockMvc.perform(post("/membership")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("회원 맴버쉽 포인트 추가-성공")
    void memberShip_add_Point() throws Exception {
        MemberShipForm form = MemberShipForm.builder().phone("010").point(10000).build();
        memberShipService.createMemberShip(form);

        String content = objectMapper.writeValueAsString(form);
        mockMvc.perform(put("/membership/point")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andDo(print())
            .andExpect(status().isOk());

        Member member = memberService.findByPhone("010");

        assertThat(member.getMemberShipPoint()).isEqualTo(20000);
    }

    @Test
    @DisplayName("회원 맴버쉽 포인트 추가-실패(회원이없음)")
    void memberShip_add_Point_not_found_member() throws Exception {
        MemberShipForm form = MemberShipForm.builder().phone("011").point(10000).build();
        String content = objectMapper.writeValueAsString(form);

        mockMvc.perform(put("/membership/point")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("회원 맴버쉽 포인트 추가-실패(회원권이없음)")
    void memberShip_add_Point_not_found_memberShip() throws Exception {
        MemberShipForm form = MemberShipForm.builder().phone("010").point(10000).build();
        String content = objectMapper.writeValueAsString(form);

        mockMvc.perform(put("/membership/point")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().is4xxClientError());
    }


}