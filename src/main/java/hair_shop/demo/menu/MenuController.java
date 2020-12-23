package hair_shop.demo.menu;

import hair_shop.apiMessage.ApiResponseMessage;
import hair_shop.demo.domain.Menu;
import hair_shop.demo.menu.validation.MenuValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MenuController {
    public final static String NOT_FOUND_MENU ="해당하는 Menu 가 존재하지않음";
    public final static String DUPLICATE_MENU ="이미 해당하는 메뉴가 있음";

    private final MenuRepository menuRepository;
    private final MenuValidation menuValidation;
    private final MenuService menuService;

    @InitBinder("menu")
    public void menuInitBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(menuValidation);
    }

    @PostMapping("/menu")
    public ResponseEntity<Object> addMenu(@RequestBody @Validated Menu menu, Errors errors){
        if(errors.hasErrors()){
            return ApiResponseMessage.error(menu.getName(),DUPLICATE_MENU);
        }
        menuRepository.save(menu);
        return ApiResponseMessage.success(menu.getName()+" Menu save");
    }

    @GetMapping("/menu")
    public ResponseEntity<Object> allMenu(){
        List<Menu> menuList = menuRepository.findAll();
        return ResponseEntity.ok(menuList);
    }

    @PutMapping("/menu/price/{name}")
    public ResponseEntity<Object> editMenuPrice(@PathVariable String name ,@RequestParam Integer price){
        Menu menu = menuRepository.findByName(name);
        if(menu==null){
            return ApiResponseMessage.error(name,NOT_FOUND_MENU);
        }
        menuService.editPriceSave(menu,price);
        return ApiResponseMessage.success(name+"의 가격이"+price+"으로 변경됨");
    }

    @PutMapping("/menu/name/{name}")
    public ResponseEntity<Object> editMenuName(@PathVariable String name ,@RequestParam String newName){
        if(menuRepository.existsByName(newName)){
            return ApiResponseMessage.error(newName,DUPLICATE_MENU);
        }
        Menu menu = menuRepository.findByName(name);
        if(menu==null){
            return ApiResponseMessage.error(name,NOT_FOUND_MENU);
        }
        menuService.editNameSave(menu,newName);
        return ApiResponseMessage.success(name+" 가 "+newName+" 으로 변경되었습니다.");
    }

}
