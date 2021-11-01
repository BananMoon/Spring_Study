package com.moonz.bookspringboot.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
// 모든 응답 Dto는 이 Dto 패키지에서!
public class HelloResponseDto {
    private final String name;
    private final int amount;
}
