package hair_shop.demo.modules.member.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class MemberForm {

    @NotBlank
    @Pattern(regexp = "^[0-9]{11}+$")
    private String phone;
    @NotBlank
    private String name;
}
