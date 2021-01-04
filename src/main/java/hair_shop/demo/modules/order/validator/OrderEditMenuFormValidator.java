package hair_shop.demo.modules.order.validator;

import hair_shop.demo.modules.menu.MenuController;
import hair_shop.demo.modules.menu.MenuRepository;
import hair_shop.demo.modules.order.OrderController;
import hair_shop.demo.modules.order.OrderRepository;
import hair_shop.demo.modules.order.form.edit.OrderMenuEditForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class OrderEditMenuFormValidator implements Validator {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(OrderMenuEditForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        OrderMenuEditForm orderEditMenuForm = (OrderMenuEditForm) o;

        if(!orderRepository.existsById(orderEditMenuForm.getOrderId())){
            errors.rejectValue("orderId",orderEditMenuForm.getOrderId().toString() , OrderController.NOT_FOUND_ORDER);
        }

        if(!menuRepository.existsByName(orderEditMenuForm.getMenuName())){
            errors.rejectValue("menuName",orderEditMenuForm.getMenuName(), MenuController.NOT_FOUND_MENU);
        }

    }
}
