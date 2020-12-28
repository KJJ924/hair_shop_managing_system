package hair_shop.demo.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import hair_shop.demo.order.form.Payment;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@EqualsAndHashCode(of = "id")
@Getter @Setter
@Builder @AllArgsConstructor
@NoArgsConstructor
@NamedEntityGraph(name = "order.withAll",attributeNodes = {
        @NamedAttributeNode("menus"),
})
public class OrderTable {

    @Id @GeneratedValue
    private Long id;

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


    public Integer totalPrice(){
        int totalPrice =0;
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
            if(orderList ==null){
                tableMap.put(dayOfMonth,new ArrayList<>(Collections.singletonList(orderTable)));
            }else {
                orderList.add(orderTable);
            }
        });
        return tableMap;
    }
    public String getMemberPhone(){
        return this.member.getPhone();
    }
    public String getMemberName(){
        return this.member.getName();
    }

    public boolean checkPayment(){
        return !this.payment.equals(Payment.NOT_PAYMENT);
    }


}
