package bigbrother.slimdealz.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CustomErrorCode {
    // user error
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"ACCOUNT-001", "사용자를 찾을 수 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-001", "토큰이 유효하지 않습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-002", "토큰이 만료되었습니다."),

    NICKNAME_ALREADY_TAKEN(HttpStatus.CONFLICT, "NICKNAME-001", "이미 사용 중인 닉네임입니다."),
    NICKNAME_TOO_SHORT(HttpStatus.BAD_REQUEST, "NICKNAME-002", "닉네임이 너무 짧습니다. 최소 길이 조건을 충족해야 합니다."),
    NICKNAME_TOO_LONG(HttpStatus.BAD_REQUEST, "NICKNAME-003", "닉네임이 너무 깁니다. 최대 길이 조건을 충족해야 합니다."),
    NICKNAME_INVALID_CHARACTERS(HttpStatus.BAD_REQUEST, "NICKNAME-004", "닉네임에 유효하지 않은 문자가 포함되어 있습니다."),
    NICKNAME_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "NICKNAME-005", "닉네임 생성에 실패했습니다."),

    CARD_TYPE_ALREADY_EXISTS(HttpStatus.CONFLICT, "CARD-001", "이미 등록된 카드 종류입니다."),
    CARD_TYPE_INVALID(HttpStatus.BAD_REQUEST, "CARD-002", "유효하지 않은 카드 종류입니다."),
    CARD_TYPE_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "CARD-003", "카드 종류 등록에 실패했습니다."),
    CARD_TYPE_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "CARD-004", "카드 종류 수정에 실패했습니다."),
    CARD_TYPE_DELETION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "CARD-005", "카드 종류 삭제에 실패했습니다."),
    CARD_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "CARD-006", "해당 카드 종류를 찾을 수 없습니다."),

    //search error
    SEARCH_NO_RESULT(HttpStatus.NOT_FOUND, "SEARCH-001", "검색 결과가 없습니다."),
    INVALID_SEARCH_KEYWORD(HttpStatus.BAD_REQUEST, "SEARCH-002", "검색어가 유효하지 않습니다."),
    SEARCH_NO_KEYWORD(HttpStatus.BAD_REQUEST, "SEARCH-003", "검색어를 입력해주세요."),
    SEARCH_TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "SEARCH-004", "검색 시간이 초과되었습니다."),

    //notification error
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "NOTIFICATION-001", "알림을 찾을 수 없습니다."),
    NOTIFICATION_ALREADY_READ(HttpStatus.BAD_REQUEST, "NOTIFICATION-002", "이미 읽은 알림입니다."),
    NOTIFICATION_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "NOTIFICATION-003", "알림 전송에 실패했습니다."),
    NOTIFICATION_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "NOTIFICATION-004", "알림 서비스가 이용 불가능합니다."),
    INVALID_NOTIFICATION_PAYLOAD(HttpStatus.BAD_REQUEST, "NOTIFICATION-005", "알림 내용이 유효하지 않습니다."),

    //product error
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT-001", "상품을 찾을 수 없습니다."),
    INVALID_PRODUCT_ID(HttpStatus.BAD_REQUEST, "PRODUCT-002", "상품이 유효하지 않습니다."),
    PRODUCT_URL_INVALID(HttpStatus.BAD_REQUEST, "PRODUCT-003", "판매처 URL이 유효하지 않습니다."),
    PRODUCT_URL_REDIRECTION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "PRODUCT-004", "판매처로의 리다이렉션에 실패했습니다."),
    PRODUCT_URL_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT-005", "판매처 URL을 찾을 수 없습니다."),
    PRODUCT_URL_TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "PRODUCT-006", "판매처 URL 접속 시간이 초과되었습니다."),
    PRODUCT_SORTING_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "PRODUCT-007", "상품 정렬에 실패했습니다."),
    PRODUCT_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT-008", "카테고리를 찾을 수 없습니다."),
    INVALID_PRODUCT_CATEGORY(HttpStatus.BAD_REQUEST, "PRODUCT-009", "카테고리가 유효하지 않습니다."),
    PRODUCT_CATEGORY_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "PRODUCT-010", "카테고리 서비스가 이용 불가능합니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
