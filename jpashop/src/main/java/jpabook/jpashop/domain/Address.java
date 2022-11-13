package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

// JPA의 내장타입
// 값 타입(Value Object)은 한번 정해지면 변경이 불가능해야한다.  => API에 노출해도 괜찮은 클래스
// JPA 스펙 상 Entity(@Entity)나 Embedded(@Embeddable) 타입은 기본 생성자가 있어야 한다.
// 이유는 JPA 구현 라이브러리가 객체를 생성할 때 리플렉션 기술을 사용할 수 있도록 가능하게 해야하기 때문.
@Embeddable
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    // 기본 클래스 (JPA 스펙 상 protected까지 허용) -> new로 생성 X
    protected Address() {}
    public Address (String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }


}
