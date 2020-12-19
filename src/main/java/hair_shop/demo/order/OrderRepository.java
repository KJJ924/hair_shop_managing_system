package hair_shop.demo.order;

import hair_shop.demo.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderTable,Long> {
}
