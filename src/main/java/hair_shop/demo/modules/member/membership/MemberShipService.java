package hair_shop.demo.modules.member.membership;

import hair_shop.demo.Infra.apiMessage.ApiResponseMessage;
import hair_shop.demo.modules.member.domain.Member;
import hair_shop.demo.modules.member.membership.domain.MemberShip;
import hair_shop.demo.modules.member.MemberController;
import hair_shop.demo.modules.member.MemberRepository;
import hair_shop.demo.modules.member.membership.form.MemberShipForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberShipService {

    private final MemberRepository memberRepository;
    private final MemberShipRepository memberShipRepository;

    public ResponseEntity<Object> createMemberShip(MemberShipForm form) {
        Member member = memberRepository.findByPhone(form.getPhone());
        if (member == null) {
            return ApiResponseMessage.error(form.getPhone(), MemberController.NOT_FOUND_MEMBER);
        } else if (member.isMemberShip()) {
            return ApiResponseMessage.error("alreadyMemberShip", MemberController.ALREADY_MEMBERSHIP);
        }
        saveMemberShip(member, form.getPoint());
        return ApiResponseMessage.success("회원권이 생성되었습니다.");
    }

    private void saveMemberShip(Member member, int point) {
        MemberShip save = memberShipRepository.save(new MemberShip(point));
        member.setMemberShip(save);
    }

    public ResponseEntity<Object> addPoint(MemberShipForm form) {
        Member member = memberRepository.findByPhone(form.getPhone());
        if (member == null) {
            return ApiResponseMessage.error(form.getPhone(), MemberController.NOT_FOUND_MEMBER);
        } else if (!member.isMemberShip()) {
            return ApiResponseMessage.error("No MemberShip", MemberController.NOT_MEMBERSHIP);
        }
        member.addPoint(form.getPoint());
        return ApiResponseMessage.success("포인트가 추가 되었습니다");
    }
}

