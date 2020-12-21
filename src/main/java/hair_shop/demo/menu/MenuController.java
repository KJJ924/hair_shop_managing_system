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
            return ResponseEntity.badRequest()
                    .body(ApiResponseMessage.builder()
                            .status("400")
                            .message("duplicate Menu")
                            .errorCode("400")
                            .errorMessage(menu.getName()+"이미 해당하는 메뉴가 있음").build());
        }
        menuRepository.save(menu);
        return ResponseEntity.ok(ApiResponseMessage.builder()
                                .status("200")
                                .message(menu.getName()+"Menu save").build());
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
            return ResponseEntity.badRequest().body(ApiResponseMessage.builder()
                    .status("400")
                    .message("not found Menu")
                    .errorCode("400")
                    .errorMessage(name+"에 해당하는 Menu 가 존재하지않음").build());
        }
        menuService.EditSave(menu,price);
        return ResponseEntity.ok().build();
    }

}
