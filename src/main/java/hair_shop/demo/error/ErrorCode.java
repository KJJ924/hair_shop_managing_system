package hair_shop.demo.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author dkansk924@naver.com
 * @since 2021/05/31
 */

@Getter
public enum ErrorCode {

    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "Entity Not Found"),

    //Member
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND,"해당하는 회원이 존재하지 않음"),
    DUPLICATE_MEMBER (HttpStatus.CONFLICT,"회원이 이미 존재함"),

    //MemberShip
    ALREADY_MEMBERSHIP(HttpStatus.CONFLICT,"이미 회원권이 존재함") ,
    NOT_MEMBERSHIP (HttpStatus.NOT_FOUND,"회원권이 없음");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
