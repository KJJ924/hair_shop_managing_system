package hair_shop.demo.modules.member.controller;

import hair_shop.demo.modules.member.domain.Member;
import hair_shop.demo.modules.member.dto.request.RequestMemberAddDescriptionForm;
import hair_shop.demo.modules.member.dto.request.RequestMemberForm;
import hair_shop.demo.modules.member.dto.response.ResponseMemberCommon;
import hair_shop.demo.modules.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Api(tags = {"memberController"})
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{phoneNumber}")
    @ApiOperation(value = "회원 조회(핸드폰번호)", notes = "회원 정보를 조회")
    public ResponseEntity<ResponseMemberCommon> getMember(
        @ApiParam(value = "회원 핸드폰번호", example = "01012345678")
        @PathVariable String phoneNumber) {
        Member member = memberService.findByPhone(phoneNumber);
        return ResponseEntity.ok(ResponseMemberCommon.toMapper(member));
    }

    @PostMapping
    @ApiOperation(value = "회원 저장", notes = "회원을 저장함")
    public ResponseEntity<ResponseMemberCommon> saveMember(
        @RequestBody @Valid RequestMemberForm requestMemberForm) {
        ResponseMemberCommon member = memberService.saveMember(requestMemberForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(member);
    }

    @GetMapping("/list")
    @ApiOperation(value = "회원 리스트 조회", notes = "모든 회원 리스트를 조회")
    public ResponseEntity<List<ResponseMemberCommon>> getMemberList() {
        return ResponseEntity.ok(memberService.getAllMemberList());
    }

    @GetMapping("/search")
    @ApiOperation(value = "회원 조회(이름)", notes = "회원 정보를 조회")
    public ResponseEntity<List<ResponseMemberCommon>> searchName(
        @ApiParam(value = "회원이름", example = "홍길동")
        @RequestParam String name) {
        return ResponseEntity.ok(memberService.getMemberSearchNameList(name));
    }

    @GetMapping("/unvisited")
    @ApiOperation(value = "최근 방문하지 않은 회원 조회", notes = "한달 이내에 방문하지 않은 회원 리스트 조회")
    public ResponseEntity<List<ResponseMemberCommon>> getMemberRecentNotComingList() {
        return ResponseEntity.ok(memberService.recentNotComingListUp());
    }

    @PutMapping("/description")
    @ApiOperation(value = "회원 특이사항 추가", notes = "회원에게 특이사항을 추가함")
    public ResponseEntity<ResponseMemberCommon> addDescription(
        @RequestBody @Valid RequestMemberAddDescriptionForm form) {
        return ResponseEntity.ok(memberService.addDescription(form));
    }
}
