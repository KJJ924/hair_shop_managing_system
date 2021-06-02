package hair_shop.demo.modules.menu.service;

import hair_shop.demo.modules.menu.domain.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuService {

    public void editPriceSave(Menu menu, Integer price) {
        menu.setPrice(price);
    }

    public void editNameSave(Menu menu, String newName) {
        menu.setName(newName);
    }
}
