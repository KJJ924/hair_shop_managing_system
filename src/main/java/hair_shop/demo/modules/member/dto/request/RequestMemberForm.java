package hair_shop.demo.modules.member.dto.request;

import hair_shop.demo.modules.member.domain.Member;
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
    private String phone;
    @NotBlank
    private String name;

    public Member toEntity(){
        return Member.builder().name(name).phone(phone).build();
    }
}
