package hair_shop.demo.modules.order.exception;

import hair_shop.demo.error.ErrorCode;
import hair_shop.demo.error.exception.EntityNotFoundException;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/10
 */
public class NotFoundOrderException extends EntityNotFoundException {

    public NotFoundOrderException() {
        super(ErrorCode.NOT_FOUND_ORDER);
    }
}
