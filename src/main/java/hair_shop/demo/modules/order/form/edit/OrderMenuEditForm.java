package hair_shop.demo.modules.order.form.edit;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class OrderMenuEditForm {

    @NotNull
    String menuName;

    Long orderId;
}
