package hair_shop.demo.modules.menu.controller;

import hair_shop.demo.modules.menu.dto.request.RequestEditMenuName;
import hair_shop.demo.modules.menu.dto.request.RequestMenu;
import hair_shop.demo.modules.menu.dto.response.ResponseMenu;
import hair_shop.demo.modules.menu.service.MenuService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/menu")
    public ResponseEntity<ResponseMenu> addMenu(@RequestBody RequestMenu menu) {
        return ResponseEntity.ok(menuService.saveMenu(menu));
    }

    @GetMapping("/menu")
    public ResponseEntity<List<ResponseMenu>> allMenu() {
        return ResponseEntity.ok(menuService.allMenu());
    }

    @PutMapping("/menu/price")
    public ResponseEntity<ResponseMenu> editMenuPrice(@RequestBody @Valid RequestMenu menu) {
        return ResponseEntity.ok(menuService.editMenuPrice(menu));
    }

    @PutMapping("/menu/name")
    public ResponseEntity<ResponseMenu> editMenuName(@RequestBody RequestEditMenuName menu) {
        return ResponseEntity.ok(menuService.editMenuName(menu));
    }

}
