package hair_shop.demo.modules.menu.repository;

import hair_shop.demo.modules.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface MenuRepository extends JpaRepository<Menu ,Long> {
    Menu findByName(String menuName);

    boolean existsByName(String name);
}
