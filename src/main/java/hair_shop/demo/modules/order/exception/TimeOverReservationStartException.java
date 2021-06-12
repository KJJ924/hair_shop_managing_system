package hair_shop.demo.modules.order.exception;

import hair_shop.demo.error.ErrorCode;
import hair_shop.demo.error.exception.BusinessException;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/09
 */
public class TimeOverReservationStartException extends BusinessException {

    public TimeOverReservationStartException() {
        super(ErrorCode.TIME_OVER_RESERVATION_START);
    }
}
