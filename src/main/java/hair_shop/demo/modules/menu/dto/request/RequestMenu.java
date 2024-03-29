package hair_shop.demo.modules.menu.dto.request;

import hair_shop.demo.modules.menu.domain.Menu;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
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

    @NotBlank
    @ApiModelProperty(value = "추가 할 메뉴이름", required = true, example = "다운펌")
    private String name;


    @ApiModelProperty(value = "메뉴가격", required = true, example = "20000")
    @Min(0)
    @NotNull
    private Integer price;

    public Menu toEntity() {
        return Menu.builder()
            .name(name)
            .price(price)
            .build();
    }

}
