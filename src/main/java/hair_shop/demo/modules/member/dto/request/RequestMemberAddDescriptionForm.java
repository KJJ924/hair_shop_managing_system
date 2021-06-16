package hair_shop.demo.modules.member.dto.request;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestMemberAddDescriptionForm {

    @NotBlank
    @Pattern(regexp = "^[0-9]{11}+$")
    @ApiModelProperty(value = "회원 핸드폰 번호", required = true, example = "01012345678")
    private String phone;

    @NotBlank
    @ApiModelProperty(value = "회원 특이사항", required = true, example = "머리 짧게 치는편!")
    private String description;
}
