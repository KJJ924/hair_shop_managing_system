package hair_shop.demo.modules.member.membership.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
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

    private int point;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime creationDate;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime expirationDate;

    private MemberShip(Integer point) {
        this.point = point;
        this.creationDate = LocalDateTime.now();
        this.expirationDate = this.creationDate.plusYears(1);
    }

    public static MemberShip create(int point){
        return new MemberShip(point);
    }
}
