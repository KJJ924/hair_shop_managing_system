package hair_shop.demo.modules.member.membership.service;

import hair_shop.demo.Infra.apiMessage.ApiResponseMessage;
import hair_shop.demo.modules.member.domain.Member;
import hair_shop.demo.modules.member.membership.repository.MemberShipRepository;
import hair_shop.demo.modules.member.membership.domain.MemberShip;
import hair_shop.demo.modules.member.MemberController;
import hair_shop.demo.modules.member.MemberRepository;
import hair_shop.demo.modules.member.membership.dto.request.MemberShipForm;
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

        //TODO [refactor]  1. 회원이 존재하는지 ? 2.  존재하는데 이미 회원권이 존재하는 회원인지 ?
        // 문제점 1. 모든 메세지 처리가 ApiResponseMessage 통해 처리되고 있음
        // 문제점 2. 반환 모델이 존재하지않음(DTO)
        // 해결방안
        // 반환 DTO 생성 및 GlobalExceptionController 통한 에러관리
        if (member == null) {
            return ApiResponseMessage.error(form.getPhone(), MemberController.NOT_FOUND_MEMBER);
        } else if (member.isMemberShip()) {
            return ApiResponseMessage.error("alreadyMemberShip", MemberController.ALREADY_MEMBERSHIP);
        }

        //TODO [refactor] 위에 존재하고 있는 예외가 다 통과한다면 회원권이 생성되어 등록된다.
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

