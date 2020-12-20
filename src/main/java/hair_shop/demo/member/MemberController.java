package hair_shop.demo.member;

import hair_shop.demo.domain.Member;
import hair_shop.demo.member.form.MemberForm;
import hair_shop.demo.member.form.MemberListInfo;
import hair_shop.demo.member.validation.MemberValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberValidation memberValidation;
    private final MemberService memberService;

    @InitBinder("memberForm")
    public void initMemberForm(WebDataBinder webDataBinder){
        webDataBinder.addValidators(memberValidation);
    }

    @GetMapping("/member/{phoneNumber}")
    public Member getMember(@PathVariable String phoneNumber){
        return memberRepository.findByPhone(phoneNumber);
    }

    @PostMapping("/member")
    public ResponseEntity saveMember(@RequestBody @Validated MemberForm memberForm , Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().build();
        }
        memberService.saveMember(memberForm);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/member/list")
    public List<MemberListInfo> getMemberList(){
       return memberService.getMemberListInfo();
    }
}
