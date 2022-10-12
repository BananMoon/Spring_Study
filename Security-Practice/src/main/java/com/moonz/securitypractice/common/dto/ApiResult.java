package com.moonz.securitypractice.common.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.moonz.securitypractice.exception.dto.ErrorResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<T> {

    private Status status;

    private T data;

    private ErrorResponseDto error;

    public enum Status {
        SUCCESS,    // 응답 성공
        FAIL,       // Client 요청 실패
        ERROR,      // Server 관련 에러
    }
    // 성공 시 데이터 없는 응답
    public static <R> ApiResult.ApiResultBuilder<R> success() {
        return ApiResult.<R>builder()
                .status(Status.SUCCESS);
    }
    // 성공 시 데이터와 함께 응답
    public static <R> ApiResult<R> success(R data) {
        return ApiResult.<R>builder()
                .status(Status.SUCCESS)
                .data(data)
                .build();
    }
}