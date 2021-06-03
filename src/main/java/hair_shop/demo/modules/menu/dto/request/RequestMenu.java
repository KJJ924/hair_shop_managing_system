package hair_shop.demo.modules.menu.dto.request;

import hair_shop.demo.modules.menu.domain.Menu;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/02
 */
@Getter
@Setter
@NoArgsConstructor
public class RequestMenu {

    @NotNull
    private String name;

    @NotNull
    private Integer price;

    public Menu toEntity() {
        return Menu.builder()
            .name(name)
            .price(price)
            .build();
    }

}
