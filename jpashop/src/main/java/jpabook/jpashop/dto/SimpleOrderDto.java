package jpabook.jpashop.dto;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SimpleOrderDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    // 중요하지 않은 Entity에서 중요한 Entity로의 의존은 ㄱㅊ
    public SimpleOrderDto(Order order) {
        orderId = order.getId();
        name = order.getMember().getName();     /* LAZY 초기화 */
        orderDate = order.getOrderDate();
        orderStatus = order.getStatus();
        address = order.getDelivery().getAddress();     /* LAZY 초기화 */
    }
}
