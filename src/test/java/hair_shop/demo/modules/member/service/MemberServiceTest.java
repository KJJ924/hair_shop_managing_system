package hair_shop.demo.modules.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import hair_shop.demo.error.ErrorCode;
import hair_shop.demo.modules.member.dto.request.RequestMemberForm;
import hair_shop.demo.modules.member.dto.response.ResponseMemberCommon;
import hair_shop.demo.modules.member.exception.DuplicationMemberException;
import hair_shop.demo.modules.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    MemberService memberService;

    @Test
    @DisplayName("Member 생성 성공")
    void saveMember() {
        //given
        memberService = new MemberService(memberRepository);
        RequestMemberForm requestMemberForm = new RequestMemberForm();
        requestMemberForm.setName("재준");
        requestMemberForm.setPhone("01000000000");

        //when
        when(memberRepository.existsByPhone(any())).thenReturn(false);
        when(memberRepository.save(any())).thenReturn(requestMemberForm.toEntity());
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
        memberService = new MemberService(memberRepository);
        RequestMemberForm requestMemberForm = new RequestMemberForm();
        requestMemberForm.setName("재준");
        requestMemberForm.setPhone("01000000000");

        //when
        when(memberRepository.existsByPhone(any())).thenReturn(true);

        //then
        assertThatThrownBy(() -> memberService.saveMember(requestMemberForm))
            .isInstanceOf(DuplicationMemberException.class)
            .hasMessage(ErrorCode.DUPLICATE_MEMBER.getMessage());
    }
}