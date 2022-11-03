package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepositoryV1;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/*
컬렉션 조회 (OneToMany) 최적화
 */
@Controller
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepositoryV1 orderRepository;
    /*
    - (본인 코드에는 반영X) Hibernate5Module을 이용해서 프록시를 강제 초기화한 값들만 값이 반환되도록 함.
    - 엔티티 직접 노출 -> 엔티티 변하면 API 스펙이 변하는 문제
    - 양방향 연관 관계 문제 해결 위해 @JsonIgnore 무조건 필요
     */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByJPQL(new OrderSearch());
        /* LAZY 초기화 */
        for (Order order : all) {
            order.getMember().getName();   /* Order:Member = N:1 */
            order.getDelivery().getAddress();   /* Order:Delivery = N:1 */
            // 추가
            List<OrderItem> orderItems = order.getOrderItems();     /* Order:OrderItem = 1:N */
            orderItems.forEach(o-> o.getItem().getName());
        }
        return all;
    }
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByJPQL(new OrderSearch());
        return orders.stream().map(o-> new OrderDto(o))
                .collect(Collectors.toList());
    }
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItem> orderItems;

        public OrderDto(Order o) {
            orderId = o.getId();
            name = o.getMember().getName();
            orderDate = o.getOrderDate();
            orderStatus = o.getStatus();
            address = o.getDelivery().getAddress();
            orderItems = o.getOrderItems();
        }

    }
}

