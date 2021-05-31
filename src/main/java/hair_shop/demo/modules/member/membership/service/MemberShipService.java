package hair_shop.demo.modules.member.membership.service;

import hair_shop.demo.Infra.apiMessage.ApiResponseMessage;
import hair_shop.demo.modules.member.MemberService;
import hair_shop.demo.modules.member.domain.Member;
import hair_shop.demo.modules.member.membership.domain.MemberShip;
import hair_shop.demo.modules.member.membership.dto.request.MemberShipForm;
import hair_shop.demo.modules.member.membership.dto.response.ResponseMemberShip;
import hair_shop.demo.modules.member.membership.error.MemberNotAlreadyException;
import hair_shop.demo.modules.member.membership.error.MemberShipAlreadyException;
import hair_shop.demo.modules.member.membership.repository.MemberShipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberShipService {

    private final MemberShipRepository memberShipRepository;
    private final MemberService memberService;

    public ResponseMemberShip createMemberShip(MemberShipForm form) {
        Member member = memberService.findByPhone(form.getPhone());

        if (member.isMemberShip()) {
            throw new MemberShipAlreadyException();
        }
        MemberShip memberShip = saveMemberShip(member, form.getPoint());
        return ResponseMemberShip.of(memberShip);
    }


    private MemberShip saveMemberShip(Member member, int point) {
        MemberShip memberShip = memberShipRepository.save(MemberShip.create(member.getPhone(),point));
        member.setMemberShip(memberShip);
        return memberShip;
    }

    public ResponseEntity<Object> addPoint(MemberShipForm form) {
        Member member = memberService.findByPhone(form.getPhone());

        if (!member.isMemberShip()) {
            throw new MemberNotAlreadyException();
        }
        member.addPoint(form.getPoint());
        return ApiResponseMessage.success("포인트가 추가 되었습니다");
    }
}

