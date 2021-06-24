package hair_shop.demo.modules.order.domain;

import hair_shop.demo.modules.menu.domain.Menu;
import hair_shop.demo.modules.menu.exception.NotFoundMenuException;
import hair_shop.demo.modules.order.orderitem.domain.OrderItem;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/24
 */
@Embeddable
public class OrderItems {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<OrderItem> orderItems = new HashSet<>();

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
    }

    public Integer totalPrice() {
        return orderItems.stream().mapToInt(OrderItem::getPrice).sum();
    }

    public boolean containsMenu(Menu menu) {
        return orderItems.stream().map(OrderItem::getMenu).anyMatch(m -> Objects.equals(m, menu));
    }

    public List<String> menuList() {
        return orderItems.stream().map(o -> o.getMenu().getName()).collect(Collectors.toList());
    }

    public void menuDelete(Menu menu) {
        OrderItem target = orderItems.stream()
            .filter(items -> Objects.equals(items.getMenu(), menu))
            .findFirst()
            .orElseThrow(NotFoundMenuException::new);
        this.orderItems.remove(target);
    }
}
