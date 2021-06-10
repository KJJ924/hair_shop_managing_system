package hair_shop.demo.modules.order.exception;

import hair_shop.demo.error.ErrorCode;
import hair_shop.demo.error.exception.BusinessException;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/10
 */
public class PaidReservationException extends BusinessException {

    public PaidReservationException() {
        super(ErrorCode.PAID_RESERVATION);
    }
}
