package hair_shop.demo.modules.designer.domain;

import javax.persistence.Column;
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
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Designer {

    @Id
    @Column(name = "designer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true , nullable = false)
    private String name;

    private Designer(String name) {
        this.name = name;
    }

    public static Designer name(String name){
        return new Designer(name);
    }
}
