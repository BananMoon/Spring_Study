package com.moonz.springPractice01.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

//Course클래스 정보를 요청할 때 사용할 DTO
@Getter
@Setter //정보 설정에 필요
@RequiredArgsConstructor
public class CourseRequestDto {
    private final String title; //final은 무조건 생성자 필요. 생성시 필수 멤버 변수이므로
    private final String tutor;

}
