package hair_shop.demo.modules.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import hair_shop.demo.modules.member.domain.Member;
import hair_shop.demo.modules.member.dto.request.RequestMemberAddDescriptionForm;
import hair_shop.demo.modules.member.dto.request.RequestMemberForm;
import hair_shop.demo.modules.member.dto.response.ResponseMemberCommon;
import hair_shop.demo.modules.member.repository.MemberRepository;
import hair_shop.demo.modules.member.service.MemberService;
import java.time.LocalDate;
import java.util.List;
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
                .name("TestMember")
                .phone("1234")
                .build();
        testMember.setLastVisitDate(LocalDate.now().minusMonths(2));

        Member testMember2 = Member.builder()
                .name("TestMember1")
                .phone("1235")
                .build();
        Member testMember3 = Member.builder()
                .name("TestMember2")
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
        RequestMemberForm requestMemberForm =new RequestMemberForm();
        requestMemberForm.setName("재준");
        requestMemberForm.setPhone("01030686687");

        mockMvc.perform(post("/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMemberForm)))
                .andExpect(status().isOk());

        Member member = memberRepository.findByPhone(requestMemberForm.getPhone()).get();
        assertThat(member).isNotNull();
    }

    @Test
    @DisplayName("회원 추가 실패(중복 회원)")
    void memberJoinWrong() throws Exception{
        RequestMemberForm requestMemberForm =new RequestMemberForm();
        requestMemberForm.setName("TestMember");
        requestMemberForm.setPhone("01000000000");

        mockMvc.perform(post("/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMemberForm)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("선택 회원 받기")
    void getMember() throws Exception{
        Member member = memberService.findByPhone("1234");
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
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("회원 리스트 받기")
    void getMemberList() throws Exception {
        List<ResponseMemberCommon> responseMemberCommon = memberService.getAllMemberList();
        String content = objectMapper.writeValueAsString(responseMemberCommon);

        mockMvc.perform(get("/member/list"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(content));
    }

    @Test
    @DisplayName("회원 리스트 받기 - 이름으로검색")
    void getMemberSearchNameList() throws Exception {
        List<ResponseMemberCommon> responseMemberCommon = memberService.getMemberSearchNameList("TestMember");
        String content = objectMapper.writeValueAsString(responseMemberCommon);

        mockMvc.perform(get("/member/search")
                .param("name","TestMember"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(content));
    }

    @Test
    @DisplayName("재방문 한달 지난 손님 리스트 받기-성공")
    void getMemberRecentNotComingList() throws Exception {
        List<ResponseMemberCommon> responseMemberCommon = memberService.recentNotComingListUp();
        String content = objectMapper.writeValueAsString(responseMemberCommon);

        mockMvc.perform(get("/member/unvisited"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(content));
    }

    @Test
    @DisplayName("회원 특이사항 추가 -성공")
    void add_memberDescription() throws Exception{
        RequestMemberAddDescriptionForm requestMemberAddDescriptionForm = new RequestMemberAddDescriptionForm();
        requestMemberAddDescriptionForm.setDescription("description");
        requestMemberAddDescriptionForm.setPhone("01000000000");

        mockMvc.perform(put("/member/description")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMemberAddDescriptionForm)))
                .andExpect(status().isOk());

        Member member = memberService.findByPhone(requestMemberAddDescriptionForm.getPhone());
        assertThat(member.getDescription()).isEqualTo(requestMemberAddDescriptionForm.getDescription());
    }

    @Test
    @DisplayName("회원 특이사항 추가 -실패(해당하는 회원이 없음)")
    void add_memberDescription_fail() throws Exception{
        RequestMemberAddDescriptionForm requestMemberAddDescriptionForm = new RequestMemberAddDescriptionForm();
        requestMemberAddDescriptionForm.setDescription("description");
        requestMemberAddDescriptionForm.setPhone("01000000001");

        mockMvc.perform(put("/member/description")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMemberAddDescriptionForm)))
                .andExpect(status().isNotFound());
    }
}