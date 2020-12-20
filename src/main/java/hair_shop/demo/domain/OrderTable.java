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
@NamedEntityGraph(name = "order.withAll",attributeNodes = {
        @NamedAttributeNode("menus"),
})
public class OrderTable {

    @Id @GeneratedValue
    private Long id;

    private LocalDateTime reservationStart;

    private LocalDateTime reservationEnd;

    private boolean status = false;

    @ManyToOne
    @JsonBackReference
    private Member  member;

    @ManyToMany
    private Set<Menu> menus;

    @ManyToOne
    private Designer designers;


    public Integer totalPrice(){
        int totalPrice =0;
        for (Menu menu : menus) {
            totalPrice += menu.getPrice();
        }
        return totalPrice;
    }
}
