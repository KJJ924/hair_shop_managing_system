package hair_shop.demo.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class OrderTable {

    @Id @GeneratedValue
    private Long id;

    private LocalDateTime reservationStart;

    private LocalDateTime reservationEnd;

    private boolean status = false;

    @ManyToOne
    @JsonBackReference
    private Member  member;

    @OneToMany
    private Set<Menu> menus;

    @ManyToOne
    private Designer designers;


}
