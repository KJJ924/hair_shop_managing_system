package hair_shop.demo.modules.designer.controller;

import hair_shop.demo.modules.designer.dto.request.RequestDesigner;
import hair_shop.demo.modules.designer.dto.response.ResponseDesigner;
import hair_shop.demo.modules.designer.service.DesignerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/designer")
public class DesignerController {

    public static final String NOT_FOUND_DESIGNER = "디자이너를 찾을수 없음";
    private final DesignerService designerService;

    @PostMapping
    public ResponseEntity<ResponseDesigner> addDesigner(@RequestBody RequestDesigner designer) {
        return ResponseEntity.ok(designerService.save(designer));
    }

    @GetMapping
    public ResponseEntity<List<ResponseDesigner>> getDesigner() {
        return ResponseEntity.ok(designerService.getDesigners());
    }

}
