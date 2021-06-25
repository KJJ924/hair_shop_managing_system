package hair_shop.demo.modules.member.membership.dto.request;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberShipForm {

    @ApiModelProperty(value = "회원 핸드폰", required = true, example = "01012345678")
    @NotBlank
    private String phone;

    @ApiModelProperty(value = "회원에게 부여할 포인트", required = true, example = "100000")
    @NotBlank
    @Min(0)
    private Integer point;

    @Builder
    public MemberShipForm(String phone, @Min(0) Integer point) {
        this.phone = phone;
        this.point = point;
    }
}
