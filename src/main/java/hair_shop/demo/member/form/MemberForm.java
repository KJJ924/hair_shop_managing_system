package hair_shop.demo.member.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MemberForm {

    @NotBlank
    private String phone;
    @NotBlank
    private String name;
}
