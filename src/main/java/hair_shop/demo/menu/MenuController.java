package hair_shop.demo.menu;

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
    public ResponseEntity addMenu(@RequestBody @Validated Menu menu, Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().build();
        }
        menuRepository.save(menu);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/menu")
    public List<Menu> allMenu(){
        return menuRepository.findAll();
    }

    @PutMapping("/menu/price/{name}")
    public ResponseEntity editMenuPrice(@PathVariable String name ,@RequestParam Integer price){
        Menu menu = menuRepository.findByName(name);
        if(menu==null){
            return ResponseEntity.badRequest().build();
        }
        menuService.EditSave(menu,price);
        return ResponseEntity.ok().build();
    }

}
