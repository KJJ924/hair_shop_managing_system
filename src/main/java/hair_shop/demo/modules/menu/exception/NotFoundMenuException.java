package hair_shop.demo.modules.menu.exception;

import hair_shop.demo.error.ErrorCode;
import hair_shop.demo.error.exception.EntityNotFoundException;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/02
 */
public class NotFoundMenuException extends EntityNotFoundException {

    public NotFoundMenuException() {
        super(ErrorCode.NOT_FOUND_MENU);
    }
}
