package hair_shop.demo.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import hair_shop.demo.domain.Member;
import hair_shop.demo.member.form.MemberAddDescriptionForm;
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
        Member testMember2 = Member.builder()
                .lastVisitDate(LocalDateTime.of(2010, 12, 10, 0, 0))
                .joinedAt(LocalDateTime.of(2020, 12, 10, 0, 0))
                .name("TestMember")
                .phone("1235")
                .build();
        Member testMember3 = Member.builder()
                .lastVisitDate(LocalDateTime.of(2010, 12, 10, 0, 0))
                .joinedAt(LocalDateTime.of(2020, 12, 10, 0, 0))
                .name("TestMember")
                .phone("01000000000")
                .build();
        memberRepository.save(testMember);
        memberRepository.save(testMember2);
        memberRepository.save(testMember3);
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
    @DisplayName("회원 추가 실패(중복 회원)")
    void memberJoinWrong() throws Exception{
        MemberForm memberForm =new MemberForm();
        memberForm.setName("TestMember");
        memberForm.setPhone("1234");

        mockMvc.perform(post("/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
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
    @DisplayName("선택 회원 받기(실패)")
    void getMember_fail() throws Exception{
        mockMvc.perform(get("/member/89898989"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
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

    @Test
    @DisplayName("재방문 한달 지난 손님 리스트 받기-성공")
    void getMemberRecentNotComingList() throws Exception {
        List<MemberListInfo> memberListInfo = memberService.recentNotComingListUp();
        String content = objectMapper.writeValueAsString(memberListInfo);

        mockMvc.perform(get("/member/recentNotComingList"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(content));
    }

    @Test
    @DisplayName("회원 특이사항 추가 -성공")
    void add_memberDescription() throws Exception{
        MemberAddDescriptionForm memberAddDescriptionForm = new MemberAddDescriptionForm();
        memberAddDescriptionForm.setDescription("description");
        memberAddDescriptionForm.setPhone("01000000000");

        mockMvc.perform(put("/member/description")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberAddDescriptionForm)))
                .andExpect(status().isOk());

        Member member = memberRepository.findByPhone(memberAddDescriptionForm.getPhone());
        assertThat(member.getDescription()).isEqualTo(memberAddDescriptionForm.getDescription());
    }

    @Test
    @DisplayName("회원 특이사항 추가 -실패(해당하는 회원이 없음)")
    void add_memberDescription_fail() throws Exception{
        MemberAddDescriptionForm memberAddDescriptionForm = new MemberAddDescriptionForm();
        memberAddDescriptionForm.setDescription("description");
        memberAddDescriptionForm.setPhone("01000000001");

        mockMvc.perform(put("/member/description")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberAddDescriptionForm)))
                .andExpect(status().isBadRequest());
    }
}