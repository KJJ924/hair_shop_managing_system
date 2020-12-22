package hair_shop.demo.member.membership;


import hair_shop.demo.domain.MemberShip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
public interface MemberShipRepository extends JpaRepository<MemberShip, Long> {
}
