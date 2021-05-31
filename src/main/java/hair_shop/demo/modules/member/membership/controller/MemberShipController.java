package hair_shop.demo.modules.member.membership.controller;

import hair_shop.demo.modules.member.membership.dto.request.MemberShipForm;
import hair_shop.demo.modules.member.membership.service.MemberShipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/membership")
public class MemberShipController {

    private final MemberShipService memberShipService;

    @PostMapping
    public ResponseEntity<Object> createMemberShip(@RequestBody MemberShipForm memberShipForm) {
        return memberShipService.createMemberShip(memberShipForm);
    }

    @PutMapping("/point")
    public ResponseEntity<Object> addPoint(@RequestBody MemberShipForm form) {
        return memberShipService.addPoint(form);
    }
}
