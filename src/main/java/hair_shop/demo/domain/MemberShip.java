package hair_shop.demo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "id")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberShip {

    @Id
    @GeneratedValue
    private Long id;

    private Integer point;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime creationDate;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime expirationDate;

    @OneToOne
    @JoinColumn(name = "id")
    private Member member;

    public MemberShip(Integer point) {
        this.point = point;
        this.creationDate = LocalDateTime.now();
        this.expirationDate = this.creationDate.plusYears(1);
    }
}
