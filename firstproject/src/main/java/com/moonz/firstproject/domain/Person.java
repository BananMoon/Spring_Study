package com.moonz.firstproject.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Person extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String job;

    // PersonController에서 Person(requestDto)로 새 데이터 생성함
    // API 응답 시 사용됨
    public Person(PersonRequestDto requestDto) {
        this.name = requestDto.getName();
        this.address = requestDto.getAddress();
        this.job = requestDto.getJob();
    }

    //매개변수있는 생성자.
    // main 함수에서 사용함!
    public Person(String name, String job, String address){
        this.name = name;
        this.address = address;
        this.job = job;
    }

    //personRequestDto로 업데이트 시
    public void update(PersonRequestDto requestDto) {
        this.name = requestDto.getName();
        this.address = requestDto.getAddress();
        this.job = requestDto.getJob();
    }
}
