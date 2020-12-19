package hair_shop.demo.member;

import hair_shop.demo.domain.Member;
import hair_shop.demo.member.form.MemberForm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;

    public void saveMember(MemberForm memberForm) {
        Member member = modelMapper.map(memberForm, Member.class);
        member.setJoinedAt(LocalDateTime.now());
        memberRepository.save(member);
    }
}
