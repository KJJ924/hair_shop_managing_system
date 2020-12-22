package hair_shop.demo.designer;

import hair_shop.apiMessage.ApiResponseMessage;
import hair_shop.demo.domain.Designer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DesignerController {

    public static final String NOT_FOUND_DESIGNER = "디자이너를 찾을수 없음";
    public static final String DUPLICATE_DESIGNER = "디자이너가 이미존재함";
    private final DesignerRepository designerRepository;

    @PostMapping("/designer")
    public ResponseEntity<Object> addDesigner(@RequestParam String name){
        if(designerRepository.existsByName(name)){
            return ApiResponseMessage.createError(name,DUPLICATE_DESIGNER);
        }
        Designer designer = Designer.builder().name(name).build();
        designerRepository.save(designer);
        return ApiResponseMessage.saveSuccess();
    }

    @GetMapping("/designer")
    public ResponseEntity<Object> getDesigner(){
        List<Designer> designers = designerRepository.findAll();
        if(designers.size() == 0){
            return ApiResponseMessage.createError("NULL",NOT_FOUND_DESIGNER);
        }
        return ResponseEntity.ok(designers);
    }

}
