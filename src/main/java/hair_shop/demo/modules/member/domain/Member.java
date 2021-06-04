package hair_shop.demo.modules.member.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import hair_shop.demo.modules.member.membership.domain.MemberShip;
import hair_shop.demo.modules.order.domain.OrderTable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@EqualsAndHashCode(of = "id")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String phone;

    @Column(nullable = false)
    private String name;

    private LocalDate lastVisitDate;

    @CreationTimestamp
    private LocalDate joinedAt;

    @OneToMany(mappedBy = "member")
    @JsonManagedReference
    private Set<OrderTable> orderList = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "memberShip_id")
    private MemberShip memberShip;

    @Lob
    private String description;

    @Builder
    public Member(String phone, String name) {
        this.phone = phone;
        this.name = name;
    }

    public boolean isMemberShip(){
        return this.memberShip != null;
    }

    public int getMemberShipPoint(){
        if(isMemberShip()) {
            return this.memberShip.getPoint();
        }
        return 0;
    }

    public void registerVisitDate(){
        this.lastVisitDate = LocalDate.now();
    }

    public void addPoint(int point) {
        memberShip.setPoint(memberShip.getPoint()+point);
        memberShip.setExpirationDate(LocalDateTime.now().plusYears(1));
    }
}
