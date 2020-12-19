package hair_shop.demo.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Member  member;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Menu> menus;

    @ManyToOne(fetch = FetchType.LAZY)
    private Designer designers;


    public Integer totalPrice(){
        int totalPrice =0;
        for (Menu menu : menus) {
            totalPrice += menu.getPrice();
        }
        return totalPrice;
    }
}
