package hair_shop.demo.modules.order;

import hair_shop.demo.modules.order.domain.OrderTable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface OrderRepository extends JpaRepository<OrderTable,Long> {

    @EntityGraph(value = "order.withAll",type = EntityGraph.EntityGraphType.LOAD)
    List<OrderTable> findByReservationDateBetweenOrderByReservationDate(LocalDate standardMonth, LocalDate plusMont);

    @Query("select a from OrderTable a " + "join fetch a.menus "+
            "where :fromDate <=a.reservationDate and a.reservationDate <:toDate ")
    List<OrderTable> findByMonthDate(@Param("fromDate") LocalDate date ,@Param("toDate") LocalDate to);
}
