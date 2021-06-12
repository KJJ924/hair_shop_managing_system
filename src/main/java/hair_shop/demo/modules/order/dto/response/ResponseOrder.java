package hair_shop.demo.modules.order.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import hair_shop.demo.modules.order.domain.OrderTable;
import hair_shop.demo.modules.order.domain.Payment;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/10
 */
@Getter
@Setter
@NoArgsConstructor
public class ResponseOrder {

    private Long orderId;
    private Payment payment;
    private List<String> menuList;
    private Integer price;
    private String memberPhoneNumber;
    private String designerName;
    @JsonFormat(pattern = "yyyy/MM/dd/HH:mm")
    private LocalDateTime reservationStart;
    @JsonFormat(pattern = "yyyy/MM/dd/HH:mm")
    private LocalDateTime reservationEnd;
    private LocalDate createAt;

    public ResponseOrder(OrderTable orderTable) {
        this.orderId = orderTable.getId();
        this.payment = orderTable.getPayment();
        this.menuList = orderTable.menuList();
        this.price = orderTable.totalPrice();
        this.memberPhoneNumber = orderTable.getMemberPhone();
        this.designerName = orderTable.getDesignerName();
        this.reservationStart = orderTable.getReservationStart();
        this.reservationEnd = orderTable.getReservationEnd();
        this.createAt = orderTable.getCreateAt();
    }

    public static ResponseOrder toMapper(OrderTable orderTable){
        return new ResponseOrder(orderTable);
    }
}
