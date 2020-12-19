package hair_shop.demo.designer;

import hair_shop.demo.domain.Designer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DesignerRepository extends JpaRepository<Designer,Long> {
    Designer findByName(String designerName);
}
