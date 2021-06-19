package hair_shop.demo.modules.member.domain;

import hair_shop.demo.modules.member.membership.domain.MemberShip;
import hair_shop.demo.modules.order.payment.exception.InsufficientPointException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String phone;

    @Column(nullable = false)
    private String name;

    private LocalDate lastVisitDate;

    @CreationTimestamp
    private LocalDate joinedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membership_id")
    private MemberShip memberShip;

    @Lob
    private String description;

    @Builder
    public Member(String phone, String name) {
        this.phone = phone;
        this.name = name;
    }

    public boolean isMemberShip() {
        return this.memberShip != null;
    }

    public int getMemberShipPoint() {
        if (isMemberShip()) {
            return this.memberShip.getPoint();
        }
        return 0;
    }

    public void addDescription(String description) {
        this.description = description;
    }

    public void updateVisitDate() {
        this.lastVisitDate = LocalDate.now();
    }

    public void addPoint(int point) {
        memberShip.setPoint(memberShip.getPoint() + point);
        memberShip.setExpirationDate(LocalDateTime.now().plusYears(1));
    }

    public int changePoint(int point) {
        if (point > getMemberShipPoint()) {
            throw new InsufficientPointException();
        }
        int result = getMemberShipPoint() - point;
        memberShip.setPoint(result);
        return result;
    }
}
