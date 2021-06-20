package hair_shop.demo.modules.order.repository;

import hair_shop.demo.modules.order.domain.Order;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface OrderRepository extends JpaRepository<Order, Long> {


    @Query("select o from Order o "
        + "join fetch o.designers "
        + "join fetch o.member "
        + "left join fetch o.orderItems i left join fetch i.menu "
        + "where o.id = :id")
    Optional<Order> findByIdWithAll(Long id);

    @EntityGraph(value = "order.withAll", type = EntityGraph.EntityGraphType.LOAD)
    List<Order> findByCreateAtBetweenOrderByCreateAt(LocalDate standardMonth, LocalDate plusMont);

    @Query("select a from Order a " + "join fetch a.orderItems " +
        "where :fromDate <=a.createAt and a.createAt <:toDate ")
    List<Order> findByMonthDate(@Param("fromDate") LocalDate date, @Param("toDate") LocalDate to);
}
