package com.moonz.firstproject.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PersonRequestDto {
    private String name;
    private String job;
    private String address;

    //생성자
    public PersonRequestDto(String name, String job, String address) {
        this.name = name;
        this.address = address;
        this.job = job;
    }
}