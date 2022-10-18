package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member {
    @Id @GeneratedValue // entity의 primary key
    @Column(name="member_id")
    private Long id;

    private String name;
    @Embedded   //내장타입 매핑
    private Address address;

    @OneToMany(mappedBy = "member") // Order클래스의 필드 member가 연관관계 주인
    private List<Order> orders = new ArrayList<>();

    public Member(String name, Address address) {
        this.name = name;
        this.address = address;
    }
}
