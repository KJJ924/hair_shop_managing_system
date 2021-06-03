package hair_shop.demo.modules.menu.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/02
 */


@Getter
@Setter
@NoArgsConstructor
public class RequestEditMenuName {

    private String originName;
    private String newName;
}
