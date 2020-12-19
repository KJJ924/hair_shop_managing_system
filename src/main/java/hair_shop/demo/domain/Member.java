package hair_shop.demo.domain;

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

    private LocalDateTime lastVisitDate;

    @OneToMany(mappedBy = "member")
    private Set<Order> orderList = new HashSet<>();

}
