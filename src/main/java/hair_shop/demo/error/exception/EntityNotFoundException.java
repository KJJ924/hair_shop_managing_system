package hair_shop.demo.error.exception;


import hair_shop.demo.error.ErrorCode;

/**
 * @author dkansk924@naver.com
 * @since 2021/05/31
 */

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException() {
        super(ErrorCode.ENTITY_NOT_FOUND);
    }

    public EntityNotFoundException(String message) {
        super(message, ErrorCode.ENTITY_NOT_FOUND);
    }

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}