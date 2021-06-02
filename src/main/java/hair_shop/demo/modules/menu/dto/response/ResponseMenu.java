package hair_shop.demo.modules.menu.dto.response;

import hair_shop.demo.modules.menu.domain.Menu;
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
public class ResponseMenu {

    private long menuId;
    private String menuName;
    private int price;

    public ResponseMenu(Menu menu) {
        this.menuId = menu.getId();
        this.menuName = menu.getName();
        this.price = menu.getPrice();
    }

    public static ResponseMenu toMapper(Menu menu) {
        return new ResponseMenu(menu);
    }
}
