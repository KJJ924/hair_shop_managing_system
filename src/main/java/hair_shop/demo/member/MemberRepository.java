package hair_shop.demo.member;


import hair_shop.demo.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Entity;

public interface MemberRepository  extends JpaRepository<Member, Long> {

    @EntityGraph(attributePaths = {"orderList","orderList.menus","orderList.designers"})
    @Query("select a from Member a")
    Member findByPhone(String phoneNumber);

    boolean existsByPhone(String phone);
}
