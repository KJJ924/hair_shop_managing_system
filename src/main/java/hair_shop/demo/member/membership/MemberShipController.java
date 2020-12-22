package hair_shop.demo.member.membership;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberShipController {

    private final MemberShipService memberShipService;

    @PostMapping("/membership")
    public ResponseEntity<Object> createMember(@RequestBody MemberShipForm memberShipForm){
        return memberShipService.getResponseToCreate(memberShipForm);
    }
}
