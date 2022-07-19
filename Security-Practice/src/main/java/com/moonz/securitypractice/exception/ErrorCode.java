package com.moonz.securitypractice.exception;

import com.moonz.securitypractice.common.Domain;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.moonz.securitypractice.common.Domain.AUTH;
import static com.moonz.securitypractice.common.Domain.COMMON;
import static javax.servlet.http.HttpServletResponse.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    HANDLE_INTERNAL_SERVER_ERROR(COMMON, 1, SC_INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다."),
    HANDLE_ACCESS_DENIED(COMMON, 2, SC_FORBIDDEN, "접근이 허용되지 않습니다."),
    INVALID_INPUT_VALUE(COMMON, 3, SC_BAD_REQUEST, "유효하지 않은 입력 값입니다."),
    INVALID_TYPE_VALUE(COMMON, 4, SC_BAD_REQUEST,  "유효하지 않은 타입입니다."),

    MEMBER_NOT_FOUND(COMMON, 5, SC_NOT_FOUND, "존재하지 않는 회원입니다."),

    REFRESH_TOKEN_IS_NULL_ERROR(AUTH, 1, SC_NOT_ACCEPTABLE, "로그아웃 상태입니다."),
    EXPIRED_TOKEN(AUTH, 2, 418, "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(AUTH, 3, SC_BAD_GATEWAY, "지원하지 않는 토큰입니다."),
    INVALID_TOKEN(AUTH, 1, SC_BAD_REQUEST, "유효하지 않는 토큰입니다."),
    SIGNATURE_ERROR(AUTH, 5, 419, "잘못된 서명입니다"),
    UNKNOWN_ERROR(AUTH, 5, SC_CONFLICT, "알 수 없는 문제가 발생했습니다"),
    ;

    private final Domain domain;

    private final int codeNum;

    private final int status;

    private final String message;

    public String getCode() {
        return String.format("%s-%03d", domain.name(), codeNum);
    }
}
