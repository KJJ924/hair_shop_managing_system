package hair_shop.demo.modules.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import hair_shop.demo.error.ErrorCode;
import hair_shop.demo.modules.member.domain.Member;
import hair_shop.demo.modules.member.dto.request.RequestMemberForm;
import hair_shop.demo.modules.member.dto.response.ResponseMemberCommon;
import hair_shop.demo.modules.member.exception.DuplicationMemberException;
import hair_shop.demo.modules.member.exception.NotFoundMemberException;
import hair_shop.demo.modules.member.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author dkansk924@naver.com
 * @since 2021/09/26
 */

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    MemberService memberService;

    @Test
    @DisplayName("Member 생성 성공")
    void saveMember() {
        //given
        RequestMemberForm requestMemberForm = new RequestMemberForm();
        requestMemberForm.setName("재준");
        requestMemberForm.setPhone("01000000000");

        given(memberRepository.existsByPhone(any())).willReturn(false);
        given(memberRepository.save(any())).willReturn(requestMemberForm.toEntity());

        //when
        ResponseMemberCommon member = memberService.saveMember(requestMemberForm);

        //then
        assertThat(member).isNotNull();
        assertThat(member.getName()).isEqualTo(requestMemberForm.getName());
        assertThat(member.getPhone()).isEqualTo(requestMemberForm.getPhone());
    }


    @Test
    @DisplayName("Member 생성 실패 - 존재하는 회원이 있는 경우")
    void saveMember_fail() {
        //given
        RequestMemberForm requestMemberForm = new RequestMemberForm();
        requestMemberForm.setName("재준");
        requestMemberForm.setPhone("01000000000");

        given(memberRepository.existsByPhone(any())).willReturn(true);

        //when
        assertThatThrownBy(() -> memberService.saveMember(requestMemberForm))
            .isInstanceOf(DuplicationMemberException.class)
            .hasMessage(ErrorCode.DUPLICATE_MEMBER.getMessage());
    }


    @Test
    @DisplayName("Member 찾기(핸드폰번호) 성공")
    void MemberFindByPhoneNumber() {
        //given
        String memberPhoneNumber = "01000000000";
        Member member = Member.builder()
            .name("재준")
            .phone(memberPhoneNumber)
            .build();

        given(memberRepository.findByPhone(memberPhoneNumber)).willReturn(Optional.of(member));

        //when
        Member findByMember = memberService.findByPhone(memberPhoneNumber);

        //then
        assertThat(findByMember).isNotNull();
        assertThat(findByMember.getPhone()).isEqualTo(memberPhoneNumber);
    }


    @Test
    @DisplayName("Member 찾기(핸드폰번호) 실패 - 존재하지 않는 회원")
    void MemberFindByPhoneNumber_fail() {
        //given
        String memberPhoneNumber = "01000000000";
        given(memberRepository.findByPhone(memberPhoneNumber)).willReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> memberService.findByPhone(memberPhoneNumber))
            .isInstanceOf(NotFoundMemberException.class)
            .hasMessage(ErrorCode.NOT_FOUND_MEMBER.getMessage());
    }
}