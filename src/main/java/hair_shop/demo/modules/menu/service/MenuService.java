package hair_shop.demo.modules.menu.service;

import hair_shop.demo.modules.menu.domain.Menu;
import hair_shop.demo.modules.menu.dto.request.RequestEditMenuName;
import hair_shop.demo.modules.menu.dto.request.RequestMenu;
import hair_shop.demo.modules.menu.dto.response.ResponseMenu;
import hair_shop.demo.modules.menu.exception.DuplicateMenuNameException;
import hair_shop.demo.modules.menu.exception.NotFoundMenuException;
import hair_shop.demo.modules.menu.repository.MenuRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    public ResponseMenu saveMenu(RequestMenu requestMenu) {
        checkMenuName(requestMenu.getName());
        Menu menu = menuRepository.save(requestMenu.toEntity());
        return ResponseMenu.toMapper(menu);
    }

    public List<ResponseMenu> allMenu() {
        List<Menu> menuList = menuRepository.findAll();

        return menuList.stream().map(ResponseMenu::new).collect(Collectors.toList());
    }

    @Transactional
    public ResponseMenu editMenuPrice(RequestMenu requestMenu) {

        Menu menu = getMenu(requestMenu.getName());
        menu.editPrice(requestMenu.getPrice());

        return ResponseMenu.toMapper(menu);
    }

    @Transactional
    public ResponseMenu editMenuName(RequestEditMenuName requestEditMenuName) {

        checkMenuName(requestEditMenuName.getNewName());

        Menu menu = getMenu(requestEditMenuName.getOriginName());
        menu.changeName(requestEditMenuName.getNewName());

        return ResponseMenu.toMapper(menu);
    }

    public Menu getMenu(String name) {
        return menuRepository.findByName(name)
            .orElseThrow(NotFoundMenuException::new);
    }

    private void checkMenuName(String name) {
        if (menuRepository.existsByName(name)) {
            throw new DuplicateMenuNameException();
        }
    }
}
