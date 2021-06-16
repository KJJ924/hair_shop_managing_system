package hair_shop.demo.modules.menu.dto.request;

import io.swagger.annotations.ApiModelProperty;
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
public class RequestEditMenuName {

    @ApiModelProperty(value = "기존 메뉴이름", required = true, example = "다운펌")
    private String originName;

    @ApiModelProperty(value = "변경할 메뉴이름", required = true, example = "다운펌+컷트")
    private String newName;
}
