package jpabook.jpashop.dto;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SimpleOrderQueryDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    // 중요하지 않은 Entity에서 중요한 Entity로의 의존은 ㄱㅊ
    public SimpleOrderQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;     /* LAZY 초기화 */
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;     /* LAZY 초기화 */
    }
}
