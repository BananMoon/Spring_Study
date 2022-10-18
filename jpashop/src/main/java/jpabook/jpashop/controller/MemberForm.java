package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * author : 문 윤지
 * description: 회원가입 dto 역할이지만 웹에 종속되어 컨트롤러에 전달할 때만 사용되는 용도로 쓰인다. (Form이라는 이름 자체에 설명이 녹아있다.)
 */
@Getter // createMemberForm.html에서 접근하기위함
@Setter // createMemberForm.html에서 객체 세팅하기 위함
public class MemberForm {
    @NotEmpty(message = "회원 이름은 필수입니다.")
    private String name;
    private String city;
    private String street;
    private String zipcode;

    private Address address;

    public Address createAddress() {
        return new Address(this.city, this.street, this.zipcode);
    }

}
