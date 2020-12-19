package hair_shop.demo.menu;

import hair_shop.demo.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu ,Long> {
    Menu findByName(String menuName);
}
