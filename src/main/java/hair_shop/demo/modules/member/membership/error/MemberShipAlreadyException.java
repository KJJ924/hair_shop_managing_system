package hair_shop.demo.modules.member.membership.error;

import hair_shop.demo.error.ErrorCode;
import hair_shop.demo.error.exception.BusinessException;

/**
 * @author dkansk924@naver.com
 * @since 2021/05/31
 */
public class MemberShipAlreadyException extends BusinessException {

    public MemberShipAlreadyException() {
        super(ErrorCode.ALREADY_MEMBERSHIP);
    }
}
