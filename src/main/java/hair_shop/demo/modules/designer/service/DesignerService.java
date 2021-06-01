package hair_shop.demo.modules.designer.service;

import hair_shop.demo.modules.designer.domain.Designer;
import hair_shop.demo.modules.designer.dto.request.RequestDesigner;
import hair_shop.demo.modules.designer.dto.response.ResponseDesigner;
import hair_shop.demo.modules.designer.exception.DuplicateDesignerException;
import hair_shop.demo.modules.designer.repository.DesignerRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/01
 */


@Service
@RequiredArgsConstructor
public class DesignerService {

    private final DesignerRepository designerRepository;

    public ResponseDesigner save(RequestDesigner requestDesigner) {
        if (designerRepository.existsByName(requestDesigner.getName())) {
            throw new DuplicateDesignerException();
        }
        return ResponseDesigner
            .toMapper(designerRepository.save(Designer.name(requestDesigner.getName())));
    }

    public List<ResponseDesigner> getDesigners() {
        List<Designer> designers = designerRepository.findAll();
        return designers.stream()
            .map(ResponseDesigner::toMapper)
            .collect(Collectors.toList());
    }
}
