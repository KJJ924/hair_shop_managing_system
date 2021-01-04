package hair_shop.demo.modules.member.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import hair_shop.demo.modules.order.domain.OrderTable;
import hair_shop.demo.modules.member.membership.domain.MemberShip;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime lastVisitDate;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime joinedAt;

    @OneToMany(mappedBy = "member")
    @JsonManagedReference
    private Set<OrderTable> orderList;

    @OneToOne
    @JoinColumn(name = "memberShip_id")
    private MemberShip memberShip;

    @Lob
    private String Description;

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
        this.lastVisitDate = LocalDateTime.now();
    }

    public void addPoint(int point) {
        memberShip.setPoint(memberShip.getPoint()+point);
        memberShip.setExpirationDate(LocalDateTime.now().plusYears(1));
    }
}
