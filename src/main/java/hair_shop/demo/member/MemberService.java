package hair_shop.demo.member;

import hair_shop.apiMessage.ApiResponseMessage;
import hair_shop.demo.domain.Member;
import hair_shop.demo.member.form.MemberAddDescriptionForm;
import hair_shop.demo.member.form.MemberForm;
import hair_shop.demo.member.form.MemberListInfo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;

    public void saveMember(MemberForm memberForm) {
        Member member = modelMapper.map(memberForm, Member.class);
        member.setJoinedAt(LocalDateTime.now());
        memberRepository.save(member);
    }

    public List<MemberListInfo> getMemberListInfo() {
        List<Member> members = memberRepository.findAll();
        List<MemberListInfo> listInfoList = new ArrayList<>();
        for (Member member : members) {
            MemberListInfo memberListInfo = modelMapper.map(member, MemberListInfo.class);
            listInfoList.add(memberListInfo);
        }
        return listInfoList;
    }

    public List<MemberListInfo> recentNotComingListUp() {
        List<Member> memberList= memberRepository.findByLastVisitDateBetween(LocalDateTime.now().minusMonths(1));

        List<MemberListInfo> listInfoList = new ArrayList<>();
        for (Member member : memberList) {
            listInfoList.add(modelMapper.map(member,MemberListInfo.class));
        }

        return listInfoList;
    }


    public ResponseEntity<Object> addDescription(MemberAddDescriptionForm form) {
        Member member = memberRepository.findByPhone(form.getPhone());
        if(member == null){
            return ApiResponseMessage.error(form.getPhone(),MemberController.NOT_FOUND_MEMBER);
        }
        member.setDescription(form.getDescription());
        return ApiResponseMessage.success("성공적으로 저장되었습니다");
    }
}
