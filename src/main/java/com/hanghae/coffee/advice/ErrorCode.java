package com.hanghae.coffee.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 참고사이트
 * <a href="https://github.com/HangHae99ProjectTeam10/SharePod-Server/blob/main/src/main/java/com/spring/sharepod/exception/CommonError/ErrorCode.java">https://github.com/HangHae99ProjectTeam10/SharePod-Server/blob/main/src/main/java/com/spring/sharepod/exception/CommonError/ErrorCode.java</a>
 * <br>
 * <pre>
 * 4xx 클라이언트 오류
 * 400 Bad Request : 잘못된 요청.
 * 401 Unauthorized : 인증이 필요함.
 * 403 Forbidden : 접근 권한 없음.
 * 404 Not Found : Resource 없음.
 * 405 Methods Not Allowed : 유효하지 않은 요청
 * 409 Conflict : 리소스 충돌(중복)
 *
 * 5xx 서버 오류
 * 500 Internal Server Error : 서버 오류 발생
 * 503 Service Unavailable : 서비스 사용 불가
 * </pre>
 */

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 공통
    COMMON_BAD_REQUEST_400(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    COMMON_BAD_REQUEST_400_FILE(HttpStatus.BAD_REQUEST, "파일업로드에 실패하였습니다."),
    COMMON_INTERNAL_ERROR_500(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생하였습니다."),

    // JWT 관련
//    JWT_BAD_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 JWT 토큰입니다."),
//    JWT_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다."),

    // NOT FOUND 관련
    NOT_FOUND_BEANS(HttpStatus.NOT_FOUND, "원두 정보가 존재하지 않습니다."),
    NOT_FOUND_OAUTH(HttpStatus.NOT_FOUND, "Oauth 타입이 존재하지 않습니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "유저 정보가 존재하지 않습니다."),
    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "게시물 정보가 존재하지 않습니다."),
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "댓글 정보가 존재하지 않습니다."),

    // FORBIDDEN 관련
    PERMISSION_DENIED(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    PERMISSION_DENIED_TO_MODIFY(HttpStatus.FORBIDDEN, "수정 권한이 없습니다."),
    PERMISSION_DENIED_TO_DELETE(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다.");



    private final HttpStatus httpStatus;
    private final String msg;
}
