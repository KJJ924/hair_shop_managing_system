package hair_shop.demo.modules.order.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import hair_shop.demo.modules.order.domain.Order;
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

    public ResponseOrder(Order order) {
        this.orderId = order.getId();
        this.payment = order.getPayment();
        this.menuList = order.menuList();
        this.price = order.totalPrice();
        this.memberPhoneNumber = order.getMemberPhone();
        this.designerName = order.getDesignerName();
        this.reservationStart = order.getReservationStart();
        this.reservationEnd = order.getReservationEnd();
        this.createAt = order.getCreateAt();
    }

    public static ResponseOrder toMapper(Order order) {
        return new ResponseOrder(order);
    }
}
