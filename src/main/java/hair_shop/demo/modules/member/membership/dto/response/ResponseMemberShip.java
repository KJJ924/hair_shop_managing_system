package hair_shop.demo.modules.member.membership.dto.response;

import hair_shop.demo.modules.member.membership.domain.MemberShip;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author dkansk924@naver.com
 * @since 2021/05/31
 */

@Getter
@NoArgsConstructor
public class ResponseMemberShip {

    private Long id;

    private String memberPhone;

    private int point;

    private LocalDateTime creationDate;

    private LocalDateTime expirationDate;

    private ResponseMemberShip(MemberShip memberShip) {
        this.id = memberShip.getId();
        this.memberPhone = memberShip.getMemberPhone();
        this.point = memberShip.getPoint();
        this.creationDate = memberShip.getCreationDate();
        this.expirationDate = memberShip.getExpirationDate();
    }

    public static ResponseMemberShip of(MemberShip memberShip) {
        return new ResponseMemberShip(memberShip);
    }
}
