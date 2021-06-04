package hair_shop.demo.modules.member.controller;

import hair_shop.demo.Infra.apiMessage.ApiResponseMessage;
import hair_shop.demo.modules.member.service.MemberService;
import hair_shop.demo.modules.member.domain.Member;
import hair_shop.demo.modules.member.dto.request.RequestMemberAddDescriptionForm;
import hair_shop.demo.modules.member.dto.request.RequestMemberForm;
import hair_shop.demo.modules.member.dto.response.ResponseMemberCommon;
import hair_shop.demo.modules.member.validation.MemberValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    public static final String NOT_FOUND_MEMBER ="해당하는 회원이 존재하지 않음";
    public static final String DUPLICATE_MEMBER ="회원이 이미 존재함";
    public static final String ALREADY_MEMBERSHIP ="이미 회원권이 존재함";
    public static final String NOT_MEMBERSHIP ="회원권이 없음";


    private final MemberValidation memberValidation;
    private final MemberService memberService;

    @InitBinder("memberForm")
    public void initMemberForm(WebDataBinder webDataBinder){
        webDataBinder.addValidators(memberValidation);
    }

    @GetMapping("/{phoneNumber}")
    public ResponseEntity<Object> getMember(@PathVariable String phoneNumber){
        Member member = memberService.findByPhone(phoneNumber);
        return ResponseEntity.ok(member);
    }

    @PostMapping
    public ResponseEntity<Object> saveMember(@RequestBody @Validated RequestMemberForm requestMemberForm, Errors errors){
        if(errors.hasErrors()){
            return ApiResponseMessage.error(errors);
        }
        memberService.saveMember(requestMemberForm);
        return ApiResponseMessage.success("성공적으로 저장됨");
    }

    @GetMapping("/list")
    public ResponseEntity<Object> getMemberList(@RequestParam(required = false) String name){
        if(name !=null){
           return ResponseEntity.ok(memberService.getMemberSearchNameList(name));
        }
        return ResponseEntity.ok(memberService.getAllMemberList());
    }

    @GetMapping("/recentNotComingList")
    public ResponseEntity<Object> getMemberRecentNotComingList(){
        List<ResponseMemberCommon>  listInfoList = memberService.recentNotComingListUp();
        if(listInfoList.isEmpty()){
            return ApiResponseMessage.success("미용실 방문 후 한달이 지난 손님이 없습니다");
        }
        return ResponseEntity.ok(listInfoList);
    }

    @PutMapping("/description")
    public ResponseEntity<Object> addDescription(@RequestBody @Valid RequestMemberAddDescriptionForm form,Errors errors){
        if(errors.hasErrors()){
            return ApiResponseMessage.error(errors);
        }
        return memberService.addDescription(form);
    }
}
