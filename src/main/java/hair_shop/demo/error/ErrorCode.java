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
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "해당하는 회원이 존재하지 않음"),
    DUPLICATE_MEMBER(HttpStatus.CONFLICT, "회원이 이미 존재함"),

    //MemberShip
    ALREADY_MEMBERSHIP(HttpStatus.CONFLICT, "이미 회원권이 존재함"),
    NOT_MEMBERSHIP(HttpStatus.NOT_FOUND, "회원권이 없음"),

    //designer
    DUPLICATE_DESIGNER(HttpStatus.CONFLICT, "디자이너가 이미존재함"),
    NOT_FOUND_DESIGNER(HttpStatus.NOT_FOUND, "디자이너를 찾을수 없음"),

    //menu
    NOT_FOUND_MENU(HttpStatus.NOT_FOUND, "해당하는 Menu 가 존재하지않음"),
    DUPLICATE_MENU(HttpStatus.CONFLICT, "이미 해당하는 메뉴가 있음"),

    //order
    PAID_RESERVATION(HttpStatus.CONFLICT, "이미 결제한 예약은 삭제가 불가능합니다"),
    NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "해당하는 예약이 없음"),
    TIME_OVER_RESERVATION_START(HttpStatus.BAD_REQUEST, "예약 시작 시간이 예약 종료시간보다 늦을수 없습니다");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
