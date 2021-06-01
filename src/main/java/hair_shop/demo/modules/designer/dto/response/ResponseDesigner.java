package hair_shop.demo.modules.designer.dto.response;

import hair_shop.demo.modules.designer.domain.Designer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/01
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseDesigner {

    private long designerId;
    private String name;

    private ResponseDesigner(Designer designer) {
        this.designerId = designer.getId();
        this.name = designer.getName();
    }

    public static ResponseDesigner toMapper(Designer designer) {
        return new ResponseDesigner(designer);
    }
}
