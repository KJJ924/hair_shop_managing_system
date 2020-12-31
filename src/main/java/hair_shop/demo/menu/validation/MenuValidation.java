package hair_shop.demo.menu.validation;

import hair_shop.demo.domain.Menu;
import hair_shop.demo.menu.MenuController;
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
            errors.rejectValue("name",menu.getName(), MenuController.DUPLICATE_MENU);
        }
        if(menu.getName().contains(" ")){
            errors.rejectValue("name",menu.getName(),"문자열안에 공백이 존재합니다.");
        }
    }
}
