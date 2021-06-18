package hair_shop.demo.modules.order.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import hair_shop.demo.modules.designer.domain.Designer;
import hair_shop.demo.modules.member.domain.Member;
import hair_shop.demo.modules.menu.domain.Menu;
import hair_shop.demo.modules.menu.exception.NotFoundMenuException;
import hair_shop.demo.modules.order.orderitem.domain.OrderItem;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
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

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "designer_id")
    private Designer designers;

    @Builder.Default
    @OneToMany(mappedBy ="order",cascade = CascadeType.REMOVE)
    private Set<OrderItem> orderItems = new HashSet<>();

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Payment payment = Payment.NOT_PAYMENT;

    @JsonFormat(pattern = "yyyy/MM/dd/HH:mm")
    private LocalDateTime reservationStart;

    @JsonFormat(pattern = "yyyy/MM/dd/HH:mm")
    private LocalDateTime reservationEnd;

    @CreationTimestamp
    private LocalDate createAt;

    public Integer totalPrice() {
        return orderItems.stream().mapToInt(OrderItem::getPrice).sum();
    }

    public static Map<Integer, List<Order>> daySeparated(List<Order> data) {
        Map<Integer, List<Order>> tableMap = new LinkedHashMap<>();
        data.forEach(orderTable -> {
            int dayOfMonth = orderTable.getReservationStart().getDayOfMonth();
            List<Order> orderList = tableMap.get(dayOfMonth);
            if (orderList == null) {
                tableMap.put(dayOfMonth, new ArrayList<>(Collections.singletonList(orderTable)));
            } else {
                orderList.add(orderTable);
            }
        });
        return tableMap;
    }

    public boolean containsMenu(Menu menu){
        return orderItems.stream().map(OrderItem::getMenu).anyMatch(m->Objects.equals(m,menu));
    }

    public void cashPayment() {
        this.payment = Payment.CASH;
        member.registerVisitDate();
    }

    public int pointPayment() {
        int remainingPoint = member.changePoint(totalPrice());
        this.payment = Payment.POINT;
        member.registerVisitDate();
        return remainingPoint;
    }

    public List<String> menuList() {
        return orderItems.stream().map(o ->o.getMenu().getName()).collect(Collectors.toList());
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


    public OrderItem menuDelete(Menu menu) {
        OrderItem target = orderItems.stream()
            .filter(items -> Objects.equals(items.getMenu(),menu))
            .findFirst()
            .orElseThrow(NotFoundMenuException::new);
        this.orderItems.remove(target);
        return target;
    }

    public void changeReservationTime(LocalDateTime start, LocalDateTime end) {
        this.reservationStart = start;
        this.reservationEnd = end;
    }
}
