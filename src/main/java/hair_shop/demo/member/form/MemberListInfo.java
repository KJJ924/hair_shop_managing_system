package hair_shop.demo.member.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberListInfo {

    private String phone;

    private String name;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime lastVisitDate;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime joinedAt;
}
