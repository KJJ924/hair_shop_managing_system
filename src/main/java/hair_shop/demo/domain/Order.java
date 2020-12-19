package hair_shop.demo.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@EqualsAndHashCode(of = "id")
@Getter @Setter
@Builder @AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id @GeneratedValue
    private Long id;

    private int Price;

    private LocalDateTime reservationStart;

    private LocalDateTime reservationEnd;

    private boolean status;

    @ManyToOne
    private Member  member;

    @OneToMany
    private Set<Menu> menus = new HashSet<>();

    @OneToMany
    private Set<Designer> designers = new HashSet<>();


}
