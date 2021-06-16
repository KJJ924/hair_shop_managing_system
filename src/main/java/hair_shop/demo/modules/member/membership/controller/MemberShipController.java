package hair_shop.demo.modules.member.membership.controller;

import hair_shop.demo.modules.member.membership.dto.request.MemberShipForm;
import hair_shop.demo.modules.member.membership.dto.response.ResponseMemberShip;
import hair_shop.demo.modules.member.membership.service.MemberShipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/membership")
@Api(tags = {"memberShipController"})
public class MemberShipController {

    private final MemberShipService memberShipService;

    @PostMapping
    @ApiOperation(value="회원권 추가", notes="선택한 회원에게 회원권을 추가합니다.")
    public ResponseEntity<ResponseMemberShip> createMemberShip(
        @RequestBody MemberShipForm memberShipForm) {
        return ResponseEntity.ok(memberShipService.createMemberShip(memberShipForm));
    }

    @PutMapping("/point")
    @ApiOperation(value="포인트 추가", notes="선택한 회원에게 입력한 포인트만큼 포인트를 추가합니다.")
    public ResponseEntity<ResponseMemberShip> addPoint(@RequestBody MemberShipForm memberShipForm) {
        return ResponseEntity.ok(memberShipService.addPoint(memberShipForm));
    }
}
