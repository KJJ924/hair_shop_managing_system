package hair_shop.demo.designer;

import hair_shop.demo.domain.Designer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface DesignerRepository extends JpaRepository<Designer,Long> {
    Designer findByName(String designerName);

    boolean existsByName(String name);
}
