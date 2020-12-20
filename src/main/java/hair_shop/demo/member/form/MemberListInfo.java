package hair_shop.demo.member.form;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberListInfo {

    private String phone;

    private String name;

    private LocalDateTime lastVisitDate;

    private LocalDateTime joinedAt;
}
