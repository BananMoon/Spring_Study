package com.moonz.securitypractice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moonz.securitypractice.exception.ErrorCode;
import com.moonz.securitypractice.exception.dto.ErrorResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 커스텀하게 Security Filter에서 발생하는 인가 예외를 핸들링하는 클래스.
 * 인가 실패 시 해당 클래스에서 HTTP 응답을 처리한다.
 */
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        ErrorResponseDto responseDto = ErrorResponseDto.of(ErrorCode.HANDLE_ACCESS_DENIED);

        String result = objectMapper.writeValueAsString(responseDto);

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write(result);
    }
}
