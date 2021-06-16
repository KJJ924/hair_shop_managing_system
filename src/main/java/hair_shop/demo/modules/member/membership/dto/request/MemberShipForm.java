package hair_shop.demo.modules.member.membership.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberShipForm {

    @ApiModelProperty(value = "회원 핸드폰", required = true, example = "01012345678")
    private String phone;

    @ApiModelProperty(value = "회원에게 부여할 포인트", required = true, example = "100000")
    private int point;
}
