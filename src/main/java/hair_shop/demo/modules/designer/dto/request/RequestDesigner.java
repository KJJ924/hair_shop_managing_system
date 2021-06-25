package hair_shop.demo.modules.designer.dto.request;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/01
 */


@Getter
@Setter
@NoArgsConstructor
public class RequestDesigner {

    @ApiModelProperty(value = "디자이너 이름", required = true, example = "디자이너")
    @NotBlank
    private String name;
}
