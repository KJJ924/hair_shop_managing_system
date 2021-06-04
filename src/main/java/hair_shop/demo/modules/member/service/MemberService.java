package hair_shop.demo.modules.member.service;

import hair_shop.demo.Infra.apiMessage.ApiResponseMessage;
import hair_shop.demo.modules.member.controller.MemberController;
import hair_shop.demo.modules.member.domain.Member;
import hair_shop.demo.modules.member.dto.request.RequestMemberAddDescriptionForm;
import hair_shop.demo.modules.member.dto.request.RequestMemberForm;
import hair_shop.demo.modules.member.dto.response.ResponseMemberCommon;
import hair_shop.demo.modules.member.exception.DuplicationMemberException;
import hair_shop.demo.modules.member.exception.NotFoundMemberException;
import hair_shop.demo.modules.member.repository.MemberRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public ResponseMemberCommon saveMember(RequestMemberForm requestMemberForm) {
        if (memberRepository.existsByPhone(requestMemberForm.getPhone())) {
            throw new DuplicationMemberException();
        }
        Member member = memberRepository.save(requestMemberForm.toEntity());
        return ResponseMemberCommon.toMapper(member);
    }

    public List<ResponseMemberCommon> getAllMemberList() {
        List<Member> members = memberRepository.findAll();
        return changeResponseMemberCommonList(members);
    }

    public List<ResponseMemberCommon> getMemberSearchNameList(String name) {
        List<Member> memberList = memberRepository.findByName(name);
        return changeResponseMemberCommonList(memberList);
    }


    public List<ResponseMemberCommon> recentNotComingListUp() {
        List<Member> memberList = memberRepository
            .findByLastVisitDateBetween(LocalDate.now().minusMonths(1));
        return changeResponseMemberCommonList(memberList);
    }


    public Member findByPhone(String phone) {
        return memberRepository.findByPhone(phone).orElseThrow(NotFoundMemberException::new);
    }

    public ResponseEntity<Object> addDescription(RequestMemberAddDescriptionForm form) {
        Member member = findByPhone(form.getPhone());
        if (member == null) {
            return ApiResponseMessage.error(form.getPhone(), MemberController.NOT_FOUND_MEMBER);
        }
        member.setDescription(form.getDescription());
        return ApiResponseMessage.success("성공적으로 저장되었습니다");
    }

    private List<ResponseMemberCommon> changeResponseMemberCommonList(List<Member> memberList) {
        return memberList.stream()
            .map(ResponseMemberCommon::toMapper)
            .collect(Collectors.toList());
    }


}
