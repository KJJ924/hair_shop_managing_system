package hair_shop.demo.modules.order.payment.exception;

import hair_shop.demo.error.ErrorCode;
import hair_shop.demo.error.exception.BusinessException;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/11
 */
public class PaymentCompleteException extends BusinessException {

    public PaymentCompleteException() {
        super(ErrorCode.PAYMENT_COMPLETE);
    }
}
