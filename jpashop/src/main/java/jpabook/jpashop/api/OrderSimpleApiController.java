package jpabook.jpashop.api;

import jpabook.jpashop.controller.OrderController;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.dto.SimpleOrderQueryDto;
import jpabook.jpashop.repository.OrderRepositoryV1;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/*
x to one (many to one, one to one)
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepositoryV1 orderRepository;

    /*
     단점
     1. Entity를 API 응답에 그대로 노출하면 안된다.
      - why? 복잡한 JSON 응답이 생성된다. 배열 안에 배열 안에 배열..
      - 실제 응답 시에는 아래와 같이 모든 Entity필드를 API에 노출하는게 아닌,
       memberName, deliveryId, ..와 같이 필요한 필드만 노출시키는게 맞다.
      {
        "member": {
          "id": 1,
          "name": "userA", ..
        },
        ..
      }
     2. Entity에서 필드에 지정한 Lazy로딩 전략으로 인해 문제 발생
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByJPQL(new OrderSearch());
        for (Order order : all) {
            System.out.println(order.getOrderItems().get(0).getItem().getName());
        }
        return all;
    }
    /*
    DTO 반환
    * 실무에서는 응답 객체로 List를 한번 더 감아야한다!
    N+1 문제
    * Order를 조회할 때 SimpleOrderDto의 필드로 인해 Member와 Delivery의 값을 조회하게 됨.
    * 그래서 각각 Member와 Delivery 조회 쿼리문이 발생하고, LAZY 초기화를 진행함.
    * 결국 Order 2개 조회 시 + Member2번 Delivery2번 조회 =>  1 + 2 + 2 = 5번 발생
    * 주문 1 + 회원 N + 배송 N
    * 1번 쿼리 실행할 때, N번 추가 쿼리가 실행되는 것이 N+1 문제이다.
     */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        List<Order> allOrders = orderRepository.findAllByJPQL(new OrderSearch());
        return allOrders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
    }

    /**
     * Order 조회 시 한번에 Member와 Delivery를 한번에 조인해서 가져온다.
     * 이때는 LAZY 를 무시하고 직접 데이터를 가져온다.
     */
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        return orders.stream().map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
    }

    /**
     * v3과 v4 비교 시, 트레이드 오프가 있음.
     * v3: 재사용성 o -> 다른 곳에서도 해당 엔티티를 호출할 수 있다.
     * v4: 즉시 DTO로 반환하만 특정 DTO로만 조회되므로 재사용성이 떨어진다.
     */
    @GetMapping("/api/v4/simple-orders")
    public List<SimpleOrderQueryDto> ordersV4() {
        return orderRepository.findOrderDtos();
    }

    @Data
    static class SimpleOrderDto {
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
}
