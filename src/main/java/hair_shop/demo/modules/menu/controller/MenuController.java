package hair_shop.demo.modules.menu.controller;

import hair_shop.demo.modules.menu.dto.request.RequestEditMenuName;
import hair_shop.demo.modules.menu.dto.request.RequestMenu;
import hair_shop.demo.modules.menu.dto.response.ResponseMenu;
import hair_shop.demo.modules.menu.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = {"menuController"})
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/menu")
    @ApiOperation(value = "메뉴 추가", notes = "새로운 메뉴를 추가합니다.")
    public ResponseEntity<ResponseMenu> addMenu(@RequestBody RequestMenu menu) {
        return ResponseEntity.ok(menuService.saveMenu(menu));
    }

    @GetMapping("/menu")
    @ApiOperation(value = "메뉴 목록 가져오기", notes = "등록된 메뉴를 모두 가져옵니다.")
    public ResponseEntity<List<ResponseMenu>> allMenu() {
        return ResponseEntity.ok(menuService.allMenu());
    }

    @PutMapping("/menu/price")
    @ApiOperation(value = "메뉴 가격 수정", notes = "입력한 가격으로 메뉴가격을 수정합니다.")
    public ResponseEntity<ResponseMenu> editMenuPrice(@RequestBody @Valid RequestMenu menu) {
        return ResponseEntity.ok(menuService.editMenuPrice(menu));
    }

    @PutMapping("/menu/name")
    @ApiOperation(value = "메뉴 이름 수정", notes = "입력한 이름으로 메뉴이름을 수정합니다.")
    public ResponseEntity<ResponseMenu> editMenuName(@RequestBody RequestEditMenuName menu) {
        return ResponseEntity.ok(menuService.editMenuName(menu));
    }

}
