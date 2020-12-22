package hair_shop.demo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@EqualsAndHashCode(of = "id")
@Getter @Setter
@Builder @AllArgsConstructor @NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true,nullable = false)
    private String phone;

    @Column(nullable = false)
    private String name;

    @JsonFormat(pattern = "yyyy/MM/dd/HH/mm")
    private LocalDateTime lastVisitDate;
    @JsonFormat(pattern = "yyyy/MM/dd/HH/mm")
    private LocalDateTime joinedAt;

    @OneToMany(mappedBy = "member")
    @JsonManagedReference
    private Set<OrderTable> orderList;

    @OneToOne
    @JoinColumn(name = "id")
    private MemberShip memberShip;
}
