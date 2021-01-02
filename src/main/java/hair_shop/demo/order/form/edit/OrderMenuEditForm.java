package hair_shop.demo.order.form.edit;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrderMenuEditForm {

    @NotNull
    String menuName;

    Long orderId;
}
