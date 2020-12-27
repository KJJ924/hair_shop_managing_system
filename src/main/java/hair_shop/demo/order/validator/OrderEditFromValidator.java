package hair_shop.demo.order.validator;

import hair_shop.demo.order.OrderController;
import hair_shop.demo.order.OrderRepository;
import hair_shop.demo.order.form.OrderEditForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OrderEditFromValidator implements Validator {

    private final OrderRepository orderRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(OrderEditForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        OrderEditForm orderForm = (OrderEditForm)o;

        if(isAfter(orderForm)){
            errors.rejectValue("reservationStart",orderForm.getReservationStart().toString()
                    ,"예약 시작 시간이 예약 종료시간보다 늦을수 없습니다");
        }
        if(!Objects.isNull(orderForm.getId())){
            if(!orderRepository.existsById(orderForm.getId())){
                errors.rejectValue("id",orderForm.getId().toString()
                        , OrderController.NOT_FOUND_ORDER);
            }
        }
    }

    private boolean isAfter(OrderEditForm orderForm) {
        return orderForm.getReservationStart().isAfter(orderForm.getReservationEnd());
    }
}
