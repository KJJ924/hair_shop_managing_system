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

    Optional<Member> findByPhone(@Param("phoneNumber") String phoneNumber);

    boolean existsByPhone(String phone);

    @Query("select a from Member a where a.lastVisitDate <:recentTime")
    List<Member> findByLastVisitDateBetween(@Param("recentTime") LocalDate now);

    List<Member> findByName(String name);
}
