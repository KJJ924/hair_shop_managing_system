package hair_shop.demo.menu;

import hair_shop.demo.domain.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuService {

    public void EditSave(Menu menu, Integer price) {
        menu.setPrice(price);
    }
}
