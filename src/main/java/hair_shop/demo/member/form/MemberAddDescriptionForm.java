package hair_shop.demo.member.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class MemberAddDescriptionForm {

    @NotBlank
    @Pattern(regexp = "^[0-9]{11}+$")
    private String phone;

    @NotBlank
    private String description;
}
