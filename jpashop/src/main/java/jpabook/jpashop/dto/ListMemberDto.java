package jpabook.jpashop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data   // ObjectMapper 인터페이스를 이용해서 API 응답 시 객체의 getter 메서드를 이용해서 JSON으로 변환
@AllArgsConstructor
public class ListMemberDto {
    private String name;
}
