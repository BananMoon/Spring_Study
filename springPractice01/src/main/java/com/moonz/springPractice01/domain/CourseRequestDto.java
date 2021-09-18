package com.moonz.springPractice01.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

//Course클래스 정보를 요청할 때 사용할 DTO
@NoArgsConstructor
@Getter
public class CourseRequestDto {
    private String title; //final은 무조건 생성자 필요. 생성시 필수 멤버 변수이므로
    private String tutor;

    public CourseRequestDto(String title, String tutor) {
        this.title = title;
        this.tutor = tutor;
    }
}

// @RequiredArgsConstructor : 이 어노테이션은 초기화 되지않은 final 필드나, @NonNull 이 붙은 필드에 대해 생성자를 생성(주입)
    // 장) 중복해서 작성해야 하는 슈가 코드를 줄일 수 있음
    //     역으로 리팩터링하여 주입이 더 이상 안필요할 때 수정해야 할 것을 하나의 수정으로 해결 할 수 있음
    // 단) 상속 받은 클래스에서는 적용이 안된다.

