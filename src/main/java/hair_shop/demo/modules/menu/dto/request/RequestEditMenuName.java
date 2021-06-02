package hair_shop.demo.modules.menu.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/02
 */


@Getter
@NoArgsConstructor
public class RequestEditMenuName {

    private String originName;
    private String newName;
}
