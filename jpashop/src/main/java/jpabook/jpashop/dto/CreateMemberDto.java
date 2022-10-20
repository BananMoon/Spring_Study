package jpabook.jpashop.dto;

import jpabook.jpashop.domain.Address;
import lombok.Getter;
import lombok.Setter;

public class CreateMemberDto {
    @Getter
    @Setter
    public static class Request {
        private String name;
        private Address address;
    }
    @Getter
    @Setter
    public static class Response {
        private Long id;
    }
}
