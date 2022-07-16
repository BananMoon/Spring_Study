package com.moonz.securitypractice.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MemberResponseDto {
    private Long id;
    private String username;
    private String email;
    private String name;

}
