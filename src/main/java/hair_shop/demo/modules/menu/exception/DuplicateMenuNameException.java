package hair_shop.demo.modules.menu.exception;

import hair_shop.demo.error.ErrorCode;
import hair_shop.demo.error.exception.BusinessException;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/02
 */
public class DuplicateMenuNameException extends BusinessException {

    public DuplicateMenuNameException() {
        super(ErrorCode.DUPLICATE_MENU);
    }
}
