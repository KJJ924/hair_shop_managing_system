package hair_shop.demo.modules.member.service;

import hair_shop.demo.Infra.apiMessage.ApiResponseMessage;
import hair_shop.demo.modules.member.controller.MemberController;
import hair_shop.demo.modules.member.domain.Member;
import hair_shop.demo.modules.member.exception.NotFoundMemberException;
import hair_shop.demo.modules.member.dto.request.RequestMemberAddDescriptionForm;
import hair_shop.demo.modules.member.dto.request.RequestMemberForm;
import hair_shop.demo.modules.member.dto.response.ResponseMemberCommon;
import hair_shop.demo.modules.member.repository.MemberRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;

    public void saveMember(RequestMemberForm requestMemberForm) {
        Member member = modelMapper.map(requestMemberForm, Member.class);
        memberRepository.save(member);
    }

    public List<ResponseMemberCommon> getAllMemberList() {
        List<Member> members = memberRepository.findAll();
        return getMemberListInfo(members);
    }

    public List<ResponseMemberCommon> getMemberSearchNameList(String name) {
        List<Member> memberList = memberRepository.findByName(name);
        return getMemberListInfo(memberList);
    }

    private List<ResponseMemberCommon> getMemberListInfo(List<Member> members) {
        List<ResponseMemberCommon> listInfoList = new ArrayList<>();
        for (Member member : members) {
            ResponseMemberCommon responseMemberCommon = modelMapper.map(member, ResponseMemberCommon.class);
            listInfoList.add(responseMemberCommon);
        }
        return listInfoList;
    }

    public List<ResponseMemberCommon> recentNotComingListUp() {
        List<Member> memberList = memberRepository
            .findByLastVisitDateBetween(LocalDate.now().minusMonths(1));

        List<ResponseMemberCommon> listInfoList = new ArrayList<>();
        for (Member member : memberList) {
            listInfoList.add(modelMapper.map(member, ResponseMemberCommon.class));
        }

        return listInfoList;
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


}
