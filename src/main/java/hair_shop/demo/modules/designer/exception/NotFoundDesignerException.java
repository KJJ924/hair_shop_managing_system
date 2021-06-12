package hair_shop.demo.modules.designer.exception;

import hair_shop.demo.error.ErrorCode;
import hair_shop.demo.error.exception.EntityNotFoundException;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/09
 */
public class NotFoundDesignerException extends EntityNotFoundException {

    public NotFoundDesignerException() {
        super(ErrorCode.DUPLICATE_DESIGNER);
    }
}
