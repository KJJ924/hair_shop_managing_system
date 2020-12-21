package hair_shop.demo.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hair_shop.demo.domain.Member;
import hair_shop.demo.member.form.MemberForm;
import hair_shop.demo.member.form.MemberListInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

    @Autowired
    MemberService memberService;


    @BeforeEach
    void initMember(){
        Member testMember = Member.builder()
                .joinedAt(LocalDateTime.of(2020, 12, 10, 0, 0))
                .name("TestMember")
                .phone("1234")
                .build();
        memberRepository.save(testMember);
    }
    @AfterEach
    void deleteAll(){
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 추가")
    void memberInsert() throws Exception{
        MemberForm memberForm =new MemberForm();
        memberForm.setName("재준");
        memberForm.setPhone("01030686687");

        mockMvc.perform(post("/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberForm)))
                .andExpect(status().isOk());

        Member member = memberRepository.findByPhone(memberForm.getPhone());
        assertThat(member).isNotNull();
    }

    @Test
    @DisplayName("선택 회원 받기")
    void getMember() throws Exception{
        Member member = memberRepository.findByPhone("1234");
        String content = objectMapper.writeValueAsString(member);
        mockMvc.perform(get("/member/1234"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(content))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 리스트 받기")
    void getMemberList() throws Exception {
        List<MemberListInfo> memberListInfo = memberService.getMemberListInfo();
        String content = objectMapper.writeValueAsString(memberListInfo);

        mockMvc.perform(get("/member/list"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(content));

    }

}