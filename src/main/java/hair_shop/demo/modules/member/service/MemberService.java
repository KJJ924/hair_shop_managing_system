package hair_shop.demo.modules.member.service;

import hair_shop.demo.Infra.apiMessage.ApiResponseMessage;
import hair_shop.demo.modules.member.controller.MemberController;
import hair_shop.demo.modules.member.domain.Member;
import hair_shop.demo.modules.member.exception.NotFoundMemberException;
import hair_shop.demo.modules.member.form.MemberAddDescriptionForm;
import hair_shop.demo.modules.member.form.MemberForm;
import hair_shop.demo.modules.member.form.MemberListInfo;
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

    public void saveMember(MemberForm memberForm) {
        Member member = modelMapper.map(memberForm, Member.class);
        memberRepository.save(member);
    }

    public List<MemberListInfo> getAllMemberList() {
        List<Member> members = memberRepository.findAll();
        return getMemberListInfo(members);
    }

    public List<MemberListInfo> getMemberSearchNameList(String name) {
        List<Member> memberList = memberRepository.findByName(name);
        return getMemberListInfo(memberList);
    }

    private List<MemberListInfo> getMemberListInfo(List<Member> members) {
        List<MemberListInfo> listInfoList = new ArrayList<>();
        for (Member member : members) {
            MemberListInfo memberListInfo = modelMapper.map(member, MemberListInfo.class);
            listInfoList.add(memberListInfo);
        }
        return listInfoList;
    }

    public List<MemberListInfo> recentNotComingListUp() {
        List<Member> memberList = memberRepository
            .findByLastVisitDateBetween(LocalDate.now().minusMonths(1));

        List<MemberListInfo> listInfoList = new ArrayList<>();
        for (Member member : memberList) {
            listInfoList.add(modelMapper.map(member, MemberListInfo.class));
        }

        return listInfoList;
    }


    public Member findByPhone(String phone) {
        return memberRepository.findByPhone(phone).orElseThrow(NotFoundMemberException::new);
    }

    public ResponseEntity<Object> addDescription(MemberAddDescriptionForm form) {
        Member member = findByPhone(form.getPhone());
        if (member == null) {
            return ApiResponseMessage.error(form.getPhone(), MemberController.NOT_FOUND_MEMBER);
        }
        member.setDescription(form.getDescription());
        return ApiResponseMessage.success("성공적으로 저장되었습니다");
    }


}