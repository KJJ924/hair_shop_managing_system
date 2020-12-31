package hair_shop.demo.member.validation;

import hair_shop.demo.member.MemberController;
import hair_shop.demo.member.MemberRepository;
import hair_shop.demo.member.form.MemberForm;
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
        return aClass.isAssignableFrom(MemberForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        MemberForm memberForm = (MemberForm) o;
        boolean result = memberRepository.existsByPhone(memberForm.getPhone());
        if(result){
            errors.rejectValue("phone","phone.duplicated", MemberController.DUPLICATE_MEMBER);
        }
    }
}
