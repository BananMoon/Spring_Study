package jpabook.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 화면에 알맞게 반환하는 레파지토리 (관심사 분리-> 엔티티 조회를 통한 핵심 비즈니스 로직: OrderRepository)
 */
@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;

    /**
     * 문제) 해당 쿼리도 사실은 N+1문제가 발생한 셈이다. Order 조회 쿼리 1개에서 OrderItem 2(N)개 존재 => 추가 OrderItem 쿼리 2(N)개 생성
     */
    public List<OrderQueryDto> findOrderQueryDto() {
        /* ToOne 관계 먼저 조회 (Query 1번 -> N개 조회) */
        List<OrderQueryDto> result = findOrders();
        /* loop 돌면서 ToMany 관계 별도 처리 (Query N번) */
        result.forEach(oqd -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(oqd.getOrderId());
            oqd.setOrderItems(orderItems);
        });
        return result;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                        " from OrderItem oi" +
                        " join oi.item i" +     /* OrderItem:Item = N:1으로, toOne 관계이기 때문에 데이터 부풀려지지 않는다. */
                        " on oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
        .getResultList();
    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }

    public List<OrderQueryDto> findOrderQueryDto_optimization() {
        /* 루트(toOne) 조회 */
        List<OrderQueryDto> result = findOrders();
        /* orderItem 컬렉션을 Map으로 조회 */
        List<Long> orderIds = toOrderIds(result);
        /* where 절에서 orderId를 1대1 매칭이 아닌 in 절로 수정 */
        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(orderIds);

        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));
        return result;
    }

    private static List<Long> toOrderIds(List<OrderQueryDto> result) {
        List<Long> orderIds = result.stream()
                .map(oqd -> oqd.getOrderId())
                .collect(Collectors.toList());
        return orderIds;
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        /* 1:N 데이터 조회 */
        List<OrderItemQueryDto> orderItems = em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +     /* OrderItem:Item = N:1으로, toOne 관계이기 때문에 데이터 부풀려지지 않는다. */
                                " on oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();
        /* 메모리 상에서 같은 orderId끼리 묶어 key=orderId, value=DTO인 Map 생성 */
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));
        return orderItemMap;
    }

    /**
     * 장점 : 한 번의 쿼리로 조회된다.
     * 단점 : join 시 1대N에서 N에 맞춰 데이터가 조회됨. 그래서 페이징이 불가함.(페이징 조회 시 1에 맞춰 조회할 수 없음)
     */
    public List<OrderFlatDto> findOrderQueryDto_flat() {
        return em.createQuery(
                "select new" +
                        " jpabook.jpashop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d" +
                        " join o.orderItems oi" +
                        " join oi.item i", OrderFlatDto.class)
                .getResultList();


    }
}
