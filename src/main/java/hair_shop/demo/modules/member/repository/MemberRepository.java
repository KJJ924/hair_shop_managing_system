package hair_shop.demo.modules.member.repository;


import hair_shop.demo.modules.member.domain.Member;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface MemberRepository  extends JpaRepository<Member, Long> {
    @Query("select a from Member a LEFT join fetch  a.memberShip " +
            "LEFT join fetch a.orderList s LEFT join fetch s.menus where a.phone = :phoneNumber")
    Optional<Member> findByPhone(@Param("phoneNumber") String phoneNumber);

    boolean existsByPhone(String phone);

    Member findWithPhoneByPhone(String memberPhoneNumber);


    @Query("select a from Member a where a.lastVisitDate <:recentTime")
    List<Member> findByLastVisitDateBetween(@Param("recentTime") LocalDate now);

    List<Member> findByName(String name);
}
