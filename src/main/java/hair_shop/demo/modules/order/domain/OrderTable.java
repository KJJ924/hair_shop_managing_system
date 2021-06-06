package hair_shop.demo.modules.order.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import hair_shop.demo.modules.menu.domain.Menu;
import hair_shop.demo.modules.designer.domain.Designer;
import hair_shop.demo.modules.member.domain.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@EqualsAndHashCode(of = "id")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@NamedEntityGraph(name = "order.withAll", attributeNodes = {
    @NamedAttributeNode("menus"),
})
public class OrderTable {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDate reservationDate;

    @JsonFormat(pattern = "yyyy/MM/dd/HH:mm")
    private LocalDateTime reservationStart;
    @JsonFormat(pattern = "yyyy/MM/dd/HH:mm")
    private LocalDateTime reservationEnd;

    @Enumerated(EnumType.STRING)
    private Payment payment;

    @ManyToOne
    @JsonBackReference
    private Member member;

    @ManyToMany
    private Set<Menu> menus;

    @ManyToOne
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

    public String getMemberPhone() {
        return this.member.getPhone();
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
}
