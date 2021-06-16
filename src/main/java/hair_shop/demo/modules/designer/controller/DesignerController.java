package hair_shop.demo.modules.designer.controller;

import hair_shop.demo.modules.designer.dto.request.RequestDesigner;
import hair_shop.demo.modules.designer.dto.response.ResponseDesigner;
import hair_shop.demo.modules.designer.service.DesignerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = {"designerController"})
public class DesignerController {

    private final DesignerService designerService;

    @PostMapping
    @ApiOperation(value="디자이너 추가", notes="새로운 디자이너를 등록합니다.")
    public ResponseEntity<ResponseDesigner> addDesigner(@RequestBody RequestDesigner designer) {
        return ResponseEntity.ok(designerService.save(designer));
    }

    @GetMapping
    @ApiOperation(value="디자이너 목록 가져오기", notes="디자이너 목록을 리스트형태로 가져옵니다.")
    public ResponseEntity<List<ResponseDesigner>> getDesigner() {
        return ResponseEntity.ok(designerService.getDesigners());
    }

}
