package jpabook.jpashop.repository;

import jpabook.jpashop.domain.OrderStatus;
import lombok.Getter;
/*
 검색조건을 가진 객체
*/
@Getter
public class OrderSearch {
    // 검색 조건
    private String memberName;
    private OrderStatus orderStatus;

}
