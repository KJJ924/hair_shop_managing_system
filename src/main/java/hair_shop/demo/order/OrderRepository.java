package hair_shop.demo.order;

import hair_shop.demo.domain.OrderTable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderTable,Long> {

    @EntityGraph(attributePaths = {"menus","designers"})
    @Query("select a from OrderTable a")
    List<OrderTable> findByReservationStartBetween(LocalDateTime standardMonth, LocalDateTime plusMonth);
}
