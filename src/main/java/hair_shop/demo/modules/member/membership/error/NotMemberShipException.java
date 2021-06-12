package hair_shop.demo.modules.member.membership.error;

import hair_shop.demo.error.ErrorCode;
import hair_shop.demo.error.exception.BusinessException;

/**
 * @author dkansk924@naver.com
 * @since 2021/05/31
 */
public class NotMemberShipException extends BusinessException {

    public NotMemberShipException() {
        super(ErrorCode.NOT_MEMBERSHIP);
    }
}
