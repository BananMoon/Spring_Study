package com.moonz.blog.handler;

import com.moonz.blog.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice   // 모든 곳에서 Exception 발생 시, 해당 클래스로 들어옴
@RestController
public class GlobalExceptionHandler {
    // IllegalArgumentException이 발생하면 이 Exception에 대한 Error는 해당 함수에 전달해줌.

    @ExceptionHandler(value=IllegalArgumentException.class)
    public ResponseDto<String> handleArgumentException(IllegalArgumentException e) {
        return new ResponseDto<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }
}
