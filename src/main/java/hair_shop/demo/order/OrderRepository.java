package hair_shop.demo.order;

import hair_shop.demo.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderTable,Long> {
    List<OrderTable> findByReservationStartBetween(LocalDateTime standardMonth, LocalDateTime plusMonth);
}
