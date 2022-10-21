package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository<Item> itemRepository;
    /**
    주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.<Item>findById(itemId)
                .orElseThrow();

        // 배송정보 생성  (원래는 배송정보도 따로 입력받아야함)
        Delivery delivery = new Delivery(member.getAddress(), DeliveryStatus.READY);

        // 주문상품 생성 
        // TODO: 2022-06-19 한개만 넘기도록 제약을 두었음. (그 이상으로도 상품 선택하도록 해보자) 
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        Order order = Order.createOrder(member, delivery, orderItem);
        // 주문 저장 (cascade 옵션으로 자동으로 함께 persist됨)
        orderRepository.save(order);
        return order.getId();
    }

    /* 주문 취소 */
    // TODO: 2022-06-19 존재하지 않는 id 삭제 시 어떻게 될지 확인! 
    @Transactional
    public void cancelOrder (Long orderId) {
        Order order = orderRepository.findOne(orderId);
//        if (order == null) throw new OrderNotFoundException();
        order.cancelOrder();
    }
    // 검색
    // TODO: 2022-06-19 없는 경우, 예외처리
    public List<Order> findOrders (OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }


}
