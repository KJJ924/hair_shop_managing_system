package hair_shop.demo.modules.order.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import hair_shop.demo.modules.designer.domain.Designer;
import hair_shop.demo.modules.member.domain.Member;
import hair_shop.demo.modules.menu.domain.Menu;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NamedEntityGraph(name = "order.withAll", attributeNodes = {
    @NamedAttributeNode("menus"),
})
public class OrderTable {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "yyyy/MM/dd/HH:mm")
    private LocalDateTime reservationStart;

    @JsonFormat(pattern = "yyyy/MM/dd/HH:mm")
    private LocalDateTime reservationEnd;

    @CreationTimestamp
    private LocalDate createAt;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Payment payment = Payment.NOT_PAYMENT;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @JsonBackReference
    private Member member;

    @Builder.Default
    @ManyToMany
    private Set<Menu> menus = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "designer_id")
    private Designer designers;

    public Integer totalPrice() {
        int totalPrice = 0;
        for (Menu menu : menus) {
            totalPrice += menu.getPrice();
        }
        return totalPrice;
    }

    public static Map<Integer, List<OrderTable>> daySeparated(List<OrderTable> data) {
        Map<Integer, List<OrderTable>> tableMap = new LinkedHashMap<>();
        data.forEach(orderTable -> {
            int dayOfMonth = orderTable.getReservationStart().getDayOfMonth();
            List<OrderTable> orderList = tableMap.get(dayOfMonth);
            if (orderList == null) {
                tableMap.put(dayOfMonth, new ArrayList<>(Collections.singletonList(orderTable)));
            } else {
                orderList.add(orderTable);
            }
        });
        return tableMap;
    }

    public List<String> menuList(){
        return menus.stream().map(Menu::getName).collect(Collectors.toList());
    }
    public String getMemberPhone() {
        return this.member.getPhone();
    }

    public String getDesignerName(){
        return this.designers.getName();
    }
    public String getMemberName() {
        return this.member.getName();
    }

    public boolean checkPayment() {
        return !this.payment.equals(Payment.NOT_PAYMENT);
    }

    public boolean menuAdd(Menu menu) {
        if (!this.menus.contains(menu)) {
            menus.add(menu);
            return true;
        }
        // 이미 자료구조가 Set 이여서 중복 검사 안해도 되는데 메세지를 전달해야하니 ...
        return false;
    }


    public boolean menuDelete(Menu menu) {
        if (this.menus.contains(menu)) {
            menus.remove(menu);
            return true;
        }
        return false;
    }

    public void changeReservationTime(LocalDateTime start, LocalDateTime end) {
        this.reservationStart =start;
        this.reservationEnd= end;
    }
}
