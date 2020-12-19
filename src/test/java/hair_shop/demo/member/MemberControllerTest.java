package hair_shop.demo.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import hair_shop.demo.domain.Member;
import hair_shop.demo.member.form.MemberForm;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원 추가")
    void memberInsert() throws Exception{
        MemberForm memberForm =new MemberForm();
        memberForm.setName("재준");
        memberForm.setPhone("01030686687");

        mockMvc.perform(post("/post/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberForm)))
                .andExpect(status().isOk());

        Member member = memberRepository.findByPhone(memberForm.getPhone());
        assertThat(member).isNotNull();
    }

    @Test
    @DisplayName("회원 받기")
    void getMember() throws Exception{
        mockMvc.perform(get("/get/member/01030686687"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}