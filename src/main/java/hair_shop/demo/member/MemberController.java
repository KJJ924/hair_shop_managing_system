package hair_shop.demo.member;

import hair_shop.apiMessage.ApiResponseMessage;
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
    public ResponseEntity<Object> getMember(@PathVariable String phoneNumber){
        Member member = memberRepository.findByPhone(phoneNumber);
        if(member==null){
            return ResponseEntity.badRequest().body(
                            ApiResponseMessage.builder()
                            .status("400")
                            .message("not find Member")
                            .errorCode("400")
                            .errorMessage("해당하는 회원이 존재하지 않음").build()
                    );
        }
        return ResponseEntity.ok(member);
    }

    @PostMapping("/member")
    public ResponseEntity<Object> saveMember(@RequestBody @Validated MemberForm memberForm , Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(
                    ApiResponseMessage.builder()
                    .status("400")
                    .message("duplication Member")
                    .errorCode("400")
                    .errorMessage(memberForm.getPhone()+" 회원이 이미 존재함").build()
            );
        }
        memberService.saveMember(memberForm);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/member/list")
    public ResponseEntity<Object> getMemberList(){
        return ResponseEntity.ok(memberService.getMemberListInfo());
    }
}
