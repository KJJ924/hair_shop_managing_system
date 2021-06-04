package hair_shop.demo.modules.member.exception;

import hair_shop.demo.error.ErrorCode;
import hair_shop.demo.error.exception.BusinessException;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/05
 */
public class DuplicationMemberException extends BusinessException {

    public DuplicationMemberException() {
        super(ErrorCode.DUPLICATE_MEMBER);
    }
}
