package hair_shop.demo.modules.member.controller;

import hair_shop.demo.modules.member.domain.Member;
import hair_shop.demo.modules.member.dto.request.RequestMemberAddDescriptionForm;
import hair_shop.demo.modules.member.dto.request.RequestMemberForm;
import hair_shop.demo.modules.member.dto.response.ResponseMemberCommon;
import hair_shop.demo.modules.member.service.MemberService;
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
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{phoneNumber}")
    public ResponseEntity<Object> getMember(@PathVariable String phoneNumber) {
        Member member = memberService.findByPhone(phoneNumber);
        return ResponseEntity.ok(member);
    }

    @PostMapping
    public ResponseEntity<ResponseMemberCommon> saveMember(
        @RequestBody @Valid RequestMemberForm requestMemberForm) {
        ResponseMemberCommon member = memberService.saveMember(requestMemberForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(member);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ResponseMemberCommon>> getMemberList() {
        return ResponseEntity.ok(memberService.getAllMemberList());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ResponseMemberCommon>> searchName(@RequestParam String name) {
        return ResponseEntity.ok(memberService.getMemberSearchNameList(name));
    }

    @GetMapping("/unvisited")
    public ResponseEntity<List<ResponseMemberCommon>> getMemberRecentNotComingList() {
        return ResponseEntity.ok(memberService.recentNotComingListUp());
    }

    @PutMapping("/description")
    public ResponseEntity<ResponseMemberCommon> addDescription(
        @RequestBody @Valid RequestMemberAddDescriptionForm form) {
        return ResponseEntity.ok(memberService.addDescription(form));
    }
}
