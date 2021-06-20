package hair_shop.demo.modules.member.membership.repository;


import hair_shop.demo.modules.member.membership.domain.MemberShip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
public interface MemberShipRepository extends JpaRepository<MemberShip, Long> {

}
