package hair_shop.demo.member;

import hair_shop.apiMessage.ApiResponseMessage;
import hair_shop.demo.domain.Member;
import hair_shop.demo.member.form.MemberForm;
import hair_shop.demo.member.validation.MemberValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {
    public static final String NOT_FOUND_MEMBER ="해당하는 회원이 존재하지 않음";
    public static final String DUPLICATE_MEMBER ="회원이 이미 존재함";
    public static final String ALREADY_MEMBERSHIP ="이미 회원권이 존재함";
    public static final String NOT_MEMBERSHIP ="회원권이 없음";

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
            return ApiResponseMessage.error(phoneNumber,NOT_FOUND_MEMBER);
        }
        return ResponseEntity.ok(member);
    }

    @PostMapping("/member")
    public ResponseEntity<Object> saveMember(@RequestBody @Validated MemberForm memberForm , Errors errors){
        if(errors.hasErrors()){
            return ApiResponseMessage.error(memberForm.getPhone(),DUPLICATE_MEMBER);
        }
        memberService.saveMember(memberForm);
        return ApiResponseMessage.success("성공적으로 저장됨");
    }

    @GetMapping("/member/list")
    public ResponseEntity<Object> getMemberList(){
        return ResponseEntity.ok(memberService.getMemberListInfo());
    }
}
