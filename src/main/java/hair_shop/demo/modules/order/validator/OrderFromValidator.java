package hair_shop.demo.modules.order.validator;

import hair_shop.demo.modules.designer.controller.DesignerController;
import hair_shop.demo.modules.designer.repository.DesignerRepository;
import hair_shop.demo.modules.member.MemberController;
import hair_shop.demo.modules.member.MemberRepository;
import hair_shop.demo.modules.menu.controller.MenuController;
import hair_shop.demo.modules.menu.repository.MenuRepository;
import hair_shop.demo.modules.order.form.OrderForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor

public class OrderFromValidator implements Validator {

    private final MemberRepository memberRepository;
    private final MenuRepository menuRepository;
    private final DesignerRepository designerRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(OrderForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        OrderForm orderForm = (OrderForm)o;

        if(isAfter(orderForm)){
            errors.rejectValue("reservationStart",orderForm.getReservationStart().toString()
                    ,"예약 시작 시간이 예약 종료시간보다 늦을수 없습니다");
        }
        if(!memberRepository.existsByPhone(orderForm.getMemberPhoneNumber())){
            errors.rejectValue("memberPhoneNumber",orderForm.getMemberPhoneNumber()
                    ,MemberController.NOT_FOUND_MEMBER);
        }
        if(!menuRepository.existsByName(orderForm.getMenuName())){
            errors.rejectValue("menuName",orderForm.getMenuName()
                    ,MenuController.NOT_FOUND_MENU);
        }
        if(!designerRepository.existsByName(orderForm.getDesignerName())){
            errors.rejectValue("designerName",orderForm.getDesignerName()
                    ,DesignerController.NOT_FOUND_DESIGNER);
        }
    }

    private boolean isAfter(OrderForm orderForm) {
        return orderForm.getReservationStart().isAfter(orderForm.getReservationEnd());
    }
}
