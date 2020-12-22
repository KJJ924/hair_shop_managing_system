package hair_shop.demo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
    private LocalDateTime expirationDate;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime creationDate;

}
