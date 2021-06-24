package hair_shop.demo.modules.order.domain;

import hair_shop.demo.modules.designer.domain.Designer;
import hair_shop.demo.modules.member.domain.Member;
import hair_shop.demo.modules.member.membership.error.NotMemberShipException;
import hair_shop.demo.modules.menu.domain.Menu;
import hair_shop.demo.modules.order.orderitem.domain.OrderItem;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;


@Table(name = "Orders")
@Entity
@EqualsAndHashCode(of = "id")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NamedEntityGraph(name = "order.withAll", attributeNodes = {
    @NamedAttributeNode("orderItems"),
})
public class Order {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designer_id")
    private Designer designers;

    @Builder.Default
    @Embedded
    private OrderItems orderItems = new OrderItems();

    public void addMenu(Menu menu) {
        this.orderItems.addOrderItem(new OrderItem(this, menu));
    }

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Payment payment = Payment.NOT_PAYMENT;

    private LocalDateTime reservationStart;

    private LocalDateTime reservationEnd;

    @CreationTimestamp
    private LocalDate createAt;


    public Integer totalPrice() {
        return orderItems.totalPrice();
    }

    public boolean containsMenu(Menu menu) {
        return orderItems.containsMenu(menu);
    }

    public List<String> menuList() {
        return orderItems.menuList();
    }

    public void menuDelete(Menu menu) {
        orderItems.menuDelete(menu);
    }

    public String getMemberPhone() {
        return this.member.getPhone();
    }

    public String getDesignerName() {
        return this.designers.getName();
    }

    public String getMemberName() {
        return this.member.getName();
    }

    public boolean checkPayment() {
        return !this.payment.equals(Payment.NOT_PAYMENT);
    }


    public void changeReservationTime(LocalDateTime start, LocalDateTime end) {
        this.reservationStart = start;
        this.reservationEnd = end;
    }

    void cashPayment() {
        this.payment = Payment.CASH;
        member.updateVisitDate();
    }

    int pointPayment() {
        if (!member.isMemberShip()) {
            throw new NotMemberShipException();
        }
        int remainingPoint = member.changePoint(totalPrice());
        this.payment = Payment.POINT;
        member.updateVisitDate();
        return remainingPoint;
    }
}
