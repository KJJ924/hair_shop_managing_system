package hair_shop.demo.member;


import hair_shop.demo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository  extends JpaRepository<Member, Long> {
    Member findByPhone(String phoneNumber);

    boolean existsByPhone(String phone);
}
