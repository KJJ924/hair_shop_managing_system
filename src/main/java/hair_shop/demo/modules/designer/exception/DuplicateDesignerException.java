package hair_shop.demo.modules.designer.exception;

import hair_shop.demo.error.ErrorCode;
import hair_shop.demo.error.exception.BusinessException;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/01
 */
public class DuplicateDesignerException extends BusinessException {

    public DuplicateDesignerException() {
        super(ErrorCode.DUPLICATE_DESIGNER);
    }
}
