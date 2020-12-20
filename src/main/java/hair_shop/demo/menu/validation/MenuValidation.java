package hair_shop.demo.menu.validation;

import hair_shop.demo.domain.Menu;
import hair_shop.demo.menu.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class MenuValidation  implements Validator {

    private final MenuRepository menuRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(Menu.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Menu menu = (Menu) o;
        if(menuRepository.existsByName(menu.getName())){
            errors.rejectValue("name","name.duplicate","이미 존재하는 메뉴입니다");
        }
    }
}
