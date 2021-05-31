package hair_shop.demo.modules.member.exception;

import hair_shop.demo.error.ErrorCode;
import hair_shop.demo.error.exception.EntityNotFoundException;

/**
 * @author dkansk924@naver.com
 * @since 2021/05/31
 */
public class NotFoundMemberException extends EntityNotFoundException {

    public NotFoundMemberException() {
        super(ErrorCode.NOT_FOUND_MEMBER);
    }
}
