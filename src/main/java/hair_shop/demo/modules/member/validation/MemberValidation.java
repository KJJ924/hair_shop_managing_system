package hair_shop.demo.modules.member.validation;

import hair_shop.demo.modules.member.controller.MemberController;
import hair_shop.demo.modules.member.repository.MemberRepository;
import hair_shop.demo.modules.member.dto.request.RequestMemberForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class MemberValidation implements Validator {

    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(RequestMemberForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        RequestMemberForm requestMemberForm = (RequestMemberForm) o;
        boolean result = memberRepository.existsByPhone(requestMemberForm.getPhone());
        if(result){
            errors.rejectValue("phone","phone.duplicated", MemberController.DUPLICATE_MEMBER);
        }
    }
}
