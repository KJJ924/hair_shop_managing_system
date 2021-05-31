package hair_shop.demo.modules.member.membership.service;

import hair_shop.demo.modules.member.MemberService;
import hair_shop.demo.modules.member.domain.Member;
import hair_shop.demo.modules.member.membership.domain.MemberShip;
import hair_shop.demo.modules.member.membership.dto.request.MemberShipForm;
import hair_shop.demo.modules.member.membership.dto.response.ResponseMemberShip;
import hair_shop.demo.modules.member.membership.error.MemberNotAlreadyException;
import hair_shop.demo.modules.member.membership.error.MemberShipAlreadyException;
import hair_shop.demo.modules.member.membership.repository.MemberShipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberShipService {

    private final MemberShipRepository memberShipRepository;
    private final MemberService memberService;

    public ResponseMemberShip createMemberShip(MemberShipForm memberShipForm) {
        Member member = memberService.findByPhone(memberShipForm.getPhone());

        if (member.isMemberShip()) {
            throw new MemberShipAlreadyException();
        }
        MemberShip memberShip = saveMemberShip(member, memberShipForm.getPoint());
        return ResponseMemberShip.of(memberShip);
    }

    public ResponseMemberShip addPoint(MemberShipForm memberShipForm) {
        Member member = memberService.findByPhone(memberShipForm.getPhone());

        if (!member.isMemberShip()) {
            throw new MemberNotAlreadyException();
        }
        member.addPoint(memberShipForm.getPoint());
        return ResponseMemberShip.of(member.getMemberShip());
    }

    private MemberShip saveMemberShip(Member member, int point) {
        MemberShip memberShip = memberShipRepository.save(MemberShip.create(member.getPhone(),point));
        member.setMemberShip(memberShip);
        return memberShip;
    }
}

