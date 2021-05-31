package hair_shop.demo.modules.member.membership.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@EqualsAndHashCode(of = "id")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberShip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true ,name = "MEMBER_PHONE")
    private String memberPhone;

    private int point;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime creationDate;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime expirationDate;

    private MemberShip(String memberPhone,Integer point) {
        this.memberPhone = memberPhone;
        this.point = point;
        this.creationDate = LocalDateTime.now();
        this.expirationDate = this.creationDate.plusYears(1);
    }

    public static MemberShip create(String memberPhone,int point){
        return new MemberShip(memberPhone,point);
    }
}
