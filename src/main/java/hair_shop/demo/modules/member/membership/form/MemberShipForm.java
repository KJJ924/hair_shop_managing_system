package hair_shop.demo.modules.member.membership.form;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberShipForm {

    private String phone;

    private int point;
}
