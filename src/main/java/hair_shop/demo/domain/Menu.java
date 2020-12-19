package hair_shop.demo.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@EqualsAndHashCode(of = "id")
@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class Menu {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private int price;
}
