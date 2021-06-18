package hair_shop.demo.modules.order.orderitem.repository;

import hair_shop.demo.modules.order.orderitem.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/18
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
