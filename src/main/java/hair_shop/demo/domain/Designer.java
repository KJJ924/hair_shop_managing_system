package hair_shop.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@EqualsAndHashCode(of = "id")
@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class Designer {

    @Id @JsonIgnore
    @GeneratedValue
    private Long id;

    @Column(unique = true , nullable = false)
    private String name;
}
