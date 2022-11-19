package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepositoryV1;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderFlatDto;
import jpabook.jpashop.repository.order.query.OrderItemQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.*;

/*
컬렉션 조회 (OneToMany) 최적화
 */
@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepositoryV1 orderRepository;
    private final OrderQueryRepository orderQueryRepository;
    /**
    - (본인 코드에는 반영X) Hibernate5Module을 이용해서 프록시를 강제 초기화한 값들만 값이 반환되도록 함.
    - 엔티티 직접 노출 -> 엔티티 변하면 API 스펙이 변하는 문제
    - 양방향 연관 관계 문제 해결 위해 @JsonIgnore 무조건 필요
     */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByJPQL(new OrderSearch());
        /* LAZY 초기화 */
        for (Order order : all) {
            order.getMember().getName();   /* 강제 초기화, Order:Member = N:1 */
            order.getDelivery().getAddress();   /*  강제 초기화, Order:Delivery = N:1 */
            // 추가
            List<OrderItem> orderItems = order.getOrderItems();     /* Order:OrderItem = 1:N */
            orderItems.forEach(o-> o.getItem().getName());  /*  강제 초기화 */
        }
        return all;
    }

    /**
     * 컬렉션 타입 Order를 조회해서 DTO로 변환하여 응답한다.
     * HttpMessageNotWritableException: Could not write JSON
     * -> OrderItem은 Lazy 전략이므로 실제 값이 들어오지 않는다. Hibernate5Module 이용하면 null값으로 반환된다.
     * <실행 쿼리>
     * Order 쿼리 1개로 결과 2개
     * - 각 Member(2명)에 대한 쿼리 2개, Delivery 쿼리 2개, OrderItem(list 원소 2개) 쿼리 2개
     *                                                  - 각 OrderItem에 연결된 Item 2개씩 있으니까 총 쿼리 4개
     * => 11개
     */
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByJPQL(new OrderSearch());
        return orders.stream().map(OrderDto::new)
                .collect(toList());
    }

    /**
     * - 쿼리 결과
     * 2개의 Order 데이터가 OrderItem과 조인하면서 4개로 조회된다.
     * JPA 입장에서는 같은 order_id에 대해서는 같은 객체(참조값)로 취급한다.
     * - 단점
     * 1. 중복 데이터 => 데이터는 100개 있지만, 1대다 연관관계 맺는 데이터가 각각 2개씩 있다면, 애플리케이션에 100개가 아닌 200개를 모두 조회한다.
     *    이 데이터를 애플리케이션 단으로 모두 전송하게 된다.
     *    즉, 예시에서는 OrderItem을 기준으로 조회되는 것이다.
     *   - JPQL의 distinct 기능이 있지만, 메모리까지는 sql문 distinct 기능만 처리되어 조회된다.
     * 2. 1대다 관계에 대해 패치조인해도 페이징 처리가 잘 되지 않는다. 중복 데이터로 조회된 상태에서 페이징이 됨. (다른 연관관계는 무관)
     */
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();

        for (Order order : orders) {
            System.out.println("order ref = " + order + ", order id = " + order.getId());
        }
        return orders.stream().map(OrderDto::new)
                .collect(toList());
    }

    /*
       V3.1. 패치조인과 페이징이 모두 가능한 방법. (컬렉션 페치 조인은 페이징이 불가능 하지만 이 방법은 페이징이 가능)
       1. default_batch_fetch_size: 100 설정
         => 컬렉션 타입으로 조회된 orders와 연관관계 있는 다 객체를 조회할 때는 pk 기준 in 쿼리로 한방에 조회
          위 설정이 바로, in 쿼리 시 조회할 갯수를 설정하는 것.  ex) 100개 데이터 있는데 옵션을 10으로 설정했다면, 애플리케이션에서 for문 돌 때 10개씩 in 절을 날려서 총 10번만 쿼리를 날리면 된다.
       2. 디테일하게 배치 사이즈 설정하려면, @BatchSize 애노테이션 사용

       결국, ToOne 관계는 페치 조인+페이징이 가능하므로 페치조인으로 쿼리 수를 줄여 해결하고,
       나머지는 hibernate.default_batch_fetch_size 로 최적화 하여 N+1 문제에서 조금은 해방하자.
    */
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                        @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);   // To One 관계 먼저 조회

        for (Order order : orders) {
            System.out.println("order ref = " + order + ", order id = " + order.getId());
        }
        return orders.stream().map(OrderDto::new) /* Order가 2개 조회됐으니 2번 돌면서 Order_Item 2개 조회 */
                .collect(toList());
    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return orderQueryRepository.findOrderQueryDto();
    }

    /**
     * 컬렉션 조회 상황에서 N+1 문제 발생하는데, 이를 방지하는 V5.
     * 조회하는 중심 DTO와 toMany 관계를 맺는 DTO를 조회할 때 쿼리가 데이터 갯수만큼 생성되지 않도록 한다.
     * 어떻게? toMany 데이터들을 메모리 상에 올려서 연산을 하는 방식으로 수정한다.
     * 결과: 쿼리 2번만으로 조회 완료. (Query: 루트 1번, 컬렉션 1번)
     * 메모리 상에서는 Map을 이용해서 O(1)로 성능.
     * 패치 조인보다 필요한 필드만을 조회할 수 있다는 장점. but 패치 조인을 사용하지 않기 때문에 조금 더 코드가 복잡해진다.
     */
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryRepository.findOrderQueryDto_optimization();
    }

    /**
     * - 1차. 쿼리 1개 생성으로 완성됐지만, N 객체에 맞춰 조회되기 때문에 중복 데이터가 조회된다.
     * + OrderQueryDto로 반환하는 스펙을 어기게 된다. (OrderFlatDto로 반환..)
     * - 2차(해결): N에 맞춰 조회된 결과에 대해 메모리 상에서!! key:OrderQueryDto와 value:OrderItemQueryDto로 매핑한 후,
     * 하나의 OrderQueryDto Set 타입 객체로 다.
     *
     * - 단점
     * 애플리케이션에서 추가 작업이 크다.
     * 쿼리는 1개지만, 조인으로 인해 조회 시 중복 데이터가 추가되는 문제가 있어 데이터 갯수가 큰 경우 문제이다.
     */
    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> ordersV6() {
        List<OrderFlatDto> flats = orderQueryRepository.findOrderQueryDto_flat();

        /* collect(groupingBy(key값, value값:mapping(묶기, 결과형태)))
           => Order(DTO)가 key, OrderItem(DTO)가 value인 MAP     */
        Map<OrderQueryDto, List<OrderItemQueryDto>> orderQueryMap = flats.stream()
                .collect(groupingBy(
                        f -> new OrderQueryDto(f.getOrderId(), f.getName(), f.getOrderDate(), f.getOrderStatus(), f.getAddress()),  /* key */
                        mapping(f -> new OrderItemQueryDto(f.getOrderId(), f.getItemName(), f.getOrderPrice(), f.getCount()), toList()) /* value */
                ));
        /* map을 entrySet으로 만든 후 OrderQueryDto List타입으로 변환 */
        return orderQueryMap.entrySet().stream()
                .map(m -> new OrderQueryDto(m.getKey().getOrderId(), m.getKey().getName(), m.getKey().getOrderDate(), m.getKey().getOrderStatus(), m.getKey().getAddress(),
                        m.getValue()))
                .collect(toList());
    }
    @Getter /* 생략할 경우, no properties 에러 발생함. */
    static class OrderDto {
        private final Long orderId;
        private final String name;
        private final LocalDateTime orderDate;
        private final OrderStatus orderStatus;
        private final Address address;
        /* 엔티티 사용
        private List<OrderItem> orderItems;
        */
        private final List<OrderItemDto> orderItems;
        public OrderDto(Order o) {
            orderId = o.getId();
            name = o.getMember().getName();
            orderDate = o.getOrderDate();
            orderStatus = o.getStatus();
            address = o.getDelivery().getAddress();
            // orderItems = o.getOrderItems();     // Lazy 초기화 하지 않는 이상 프록시 객체.
            orderItems = o.getOrderItems().stream()
                    .map(OrderItemDto::new)
                    .collect(toList());

        }
    }

    /**
     * OrderItem의 필드 수정 시, API에 영향을 미치므로 OrderItemDTO로 대체해야한다.
     */
    @Getter
    static class OrderItemDto {
        private final String itemName;
        private final int orderPrice;
        private final int count;

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}

