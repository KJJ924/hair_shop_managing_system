package hair_shop.demo.member.membership;

import hair_shop.apiMessage.ApiResponseMessage;
import hair_shop.demo.domain.Member;
import hair_shop.demo.domain.MemberShip;
import hair_shop.demo.member.MemberController;
import hair_shop.demo.member.MemberRepository;
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

    public ResponseEntity<Object> getResponseToCreate(MemberShipForm form) {
        Member member = memberRepository.findByPhone(form.getPhone());
        if(member ==null){
            return ApiResponseMessage.error(form.getPhone(), MemberController.NOT_FOUND_MEMBER);
        }else if(member.getMemberShip() !=null){
            return ApiResponseMessage.error("alreadyMemberShip",MemberController.ALREADY_MEMBERSHIP);
        }
        saveMemberShip(member,form.getPoint());
        return ResponseEntity.ok().build();
    }

    private void saveMemberShip(Member member,int point){
        MemberShip save = memberShipRepository.save(new MemberShip(point));
        member.setMemberShip(save);
    }

}
