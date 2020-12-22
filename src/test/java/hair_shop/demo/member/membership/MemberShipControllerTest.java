package hair_shop.demo.member.membership;

import com.fasterxml.jackson.databind.ObjectMapper;
import hair_shop.demo.domain.Member;
import hair_shop.demo.domain.MemberShip;
import hair_shop.demo.member.MemberRepository;
import hair_shop.demo.member.MemberService;
import hair_shop.demo.member.form.MemberForm;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void initData(){
        MemberForm memberForm = new MemberForm();
        memberForm.setName("test");
        memberForm.setPhone("010");
        memberService.saveMember(memberForm);
    }

    @AfterEach
    void clearData(){
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원권 생성- 성공")
    void createMemberShip() throws Exception{
        MemberShipForm form = MemberShipForm.builder().phone("010").point(10000).build();
        String content = objectMapper.writeValueAsString(form);

        mockMvc.perform(post("/membership")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk());

        Member member = memberRepository.findByPhone(form.getPhone());
        assertThat(member.getMemberShip()).isNotNull();
        assertThat(member.getMemberShip().getPoint()).isEqualTo(10000);
    }

    @Test
    @DisplayName("회원권 생성-실패(해당 맴버가없음)")
    void createMemberShip_fail_not_found_member() throws Exception{
        MemberShipForm form = MemberShipForm.builder().phone("011").point(10000).build();
        String content = objectMapper.writeValueAsString(form);

        mockMvc.perform(post("/membership")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원권 생성-실패(회원권이 이미존재함)")
    void createMemberShip_fail_already_memberShip() throws Exception{

        MemberShipForm form = MemberShipForm.builder().phone("010").point(10000).build();
        memberShipService.getResponseToCreate(form);

        String content = objectMapper.writeValueAsString(form);
        mockMvc.perform(post("/membership")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


}