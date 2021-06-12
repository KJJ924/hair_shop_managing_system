package hair_shop.demo.modules.order.dto.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestOrderMenuEdit {

    @NotNull
    private String menuName;
    @NotNull
    private Long orderId;
}
