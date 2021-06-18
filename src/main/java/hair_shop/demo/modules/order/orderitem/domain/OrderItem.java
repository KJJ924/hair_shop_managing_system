package hair_shop.demo.modules.order.orderitem.domain;

import hair_shop.demo.modules.menu.domain.Menu;
import hair_shop.demo.modules.order.domain.Order;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/18
 */

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private int price;

    public OrderItem(Order order, Menu menu) {
        this.order = order;
        this.menu = menu;
        this.price = menu.getPrice();
    }

    public static OrderItem createOrderItem(Order order, Menu menu) {
        return new OrderItem(order, menu);
    }

}
