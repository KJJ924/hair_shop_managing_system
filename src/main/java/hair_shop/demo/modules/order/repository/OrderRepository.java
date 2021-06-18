package hair_shop.demo.modules.order.repository;

import hair_shop.demo.modules.order.domain.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface OrderRepository extends JpaRepository<Order,Long> {

    @EntityGraph(value = "order.withAll",type = EntityGraph.EntityGraphType.LOAD)
    List<Order> findByCreateAtBetweenOrderByCreateAt(LocalDate standardMonth, LocalDate plusMont);

    @Query("select a from Order a " + "join fetch a.orderItems "+
            "where :fromDate <=a.createAt and a.createAt <:toDate ")
    List<Order> findByMonthDate(@Param("fromDate") LocalDate date ,@Param("toDate") LocalDate to);
}
