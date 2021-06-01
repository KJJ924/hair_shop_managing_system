package hair_shop.demo.modules.designer.controller;

import hair_shop.demo.Infra.apiMessage.ApiResponseMessage;
import hair_shop.demo.modules.designer.domain.Designer;
import hair_shop.demo.modules.designer.repository.DesignerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/designer")
public class DesignerController {

    /*
    * TODO 1. Controller 가 가지고있는 ERROR MESSAGE 분리
    * TODO 2. save , get 로직 서비스 레이어로 이동
    * TODO 3. Response 응답 DTO 생성
    * TODO 4. Domain 객체 gen key 전략 변경
    * */
    public static final String NOT_FOUND_DESIGNER = "디자이너를 찾을수 없음";
    public static final String DUPLICATE_DESIGNER = "디자이너가 이미존재함";
    private final DesignerRepository designerRepository;

    @PostMapping
    public ResponseEntity<Object> addDesigner(@RequestParam String name){
        if(designerRepository.existsByName(name)){
            return ApiResponseMessage.error(name,DUPLICATE_DESIGNER);
        }
        Designer designer = Designer.name(name);
        designerRepository.save(designer);
        return ApiResponseMessage.success("성공적으로 저장됨");
    }

    @GetMapping
    public ResponseEntity<Object> getDesigner(){
        List<Designer> designers = designerRepository.findAll();
        if(designers.size() == 0){
            return ApiResponseMessage.error("NULL",NOT_FOUND_DESIGNER);
        }
        return ResponseEntity.ok(designers);
    }

}
