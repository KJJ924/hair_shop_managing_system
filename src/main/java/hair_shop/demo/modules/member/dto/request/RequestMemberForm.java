package hair_shop.demo.modules.member.dto.request;

import hair_shop.demo.modules.member.domain.Member;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestMemberForm {

    @NotBlank
    @Pattern(regexp = "^[0-9]{11}+$")
    @ApiModelProperty(value = "회원 핸드폰", required = true, example = "01012345678")
    private String phone;

    @NotBlank
    @ApiModelProperty(value = "회원 이름", required = true, example = "홍길동")
    private String name;

    public Member toEntity() {
        return Member.builder().name(name).phone(phone).build();
    }
}
