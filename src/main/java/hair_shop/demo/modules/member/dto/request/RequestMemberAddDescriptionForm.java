package hair_shop.demo.modules.member.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class RequestMemberAddDescriptionForm {

    @NotBlank
    @Pattern(regexp = "^[0-9]{11}+$")
    private String phone;

    @NotBlank
    private String description;
}
