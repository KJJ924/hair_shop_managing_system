package hair_shop.demo.member;


import hair_shop.demo.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Entity;

public interface MemberRepository  extends JpaRepository<Member, Long> {

    @Query("select a from Member a join fetch a.orderList s join fetch s.menus")
    Member findByPhone(String phoneNumber);

    boolean existsByPhone(String phone);
}
