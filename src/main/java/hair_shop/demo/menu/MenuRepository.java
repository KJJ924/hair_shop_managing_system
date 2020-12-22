package hair_shop.demo.menu;

import hair_shop.demo.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface MenuRepository extends JpaRepository<Menu ,Long> {
    Menu findByName(String menuName);

    boolean existsByName(String name);
}
