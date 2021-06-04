package hair_shop.demo.modules.member.dto.response;

import hair_shop.demo.modules.member.domain.Member;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseMemberCommon {

    private Long memberId;

    private String phone;

    private String name;

    private LocalDate lastVisitDate;

    private LocalDate joinedAt;

    private ResponseMemberCommon(Member member) {
        this.memberId = member.getId();
        this.phone = member.getPhone();
        this.name = member.getName();
        this.joinedAt = member.getJoinedAt();
    }

    public static ResponseMemberCommon toMapper(Member member) {
        return new ResponseMemberCommon(member);
    }
}
