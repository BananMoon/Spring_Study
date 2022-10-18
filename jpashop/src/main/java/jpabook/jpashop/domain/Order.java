package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
public class Order {
    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)  // Order:Member. ManyToOne은 디폴트가 EAGER이므로 주의!
    @JoinColumn(name="member_id")   // 연관관계맺는 칼럼
    private Member member;

    // cascade = CascadeType.ALL 옵션 아래 설명 참고!
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    // cascade = CascadeType.ALL 옵션!!
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="delivery_id")
    private Delivery delivery;

    @Setter
    private LocalDateTime orderDate;    // 주문 시간. hibernate가 DateTime의 형태를 자동 매핑해줌. (java8~)

    @Setter
    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문 상태. 열거형

    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }
    public void addOrderItem (OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);   // 모든 OrderItem에 Order(주문) 정보 세팅
    }
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }
    private Order(OrderStatus status, LocalDateTime orderDate) {
        this.status = status;
        this.orderDate = orderDate;
    }
    // TODO: 2022-06-19 인자로 OrderItemDto가 넘어오는 식으로 되서 더 복잡한 로직이 됨.
    //==생성 메서드(정적 팩토리 메서드)==//
    // OrderItem을 먼저 만들고, Order 객체를 만든다.
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order(OrderStatus.ORDERED, LocalDateTime.now());
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        return order;
    }
    //==비즈니스 로직==//
    /**
     * 주문 취소
     */
    public void cancelOrder() {
        if (delivery.getStatus() == DeliveryStatus.COMPLETE){
            throw new IllegalStateException("이미 배송완료된 상품은 취소 불가합니다.");
        }
        this.status = OrderStatus.CANCEL;   // JPA 더티체킹-update query문 전송
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel(); // 재고수량 원복
        }
    }
    //==조회 로직==//
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        return orderItems.stream().
                mapToInt(OrderItem::getItemsPrice)
                .sum();
    }
}

/* cascade = CascadeType.ALL 옵션!
     orderItem A, B, C(혹은 Delivery)를 각각 persist하고, order를 persist할 필요없이,
     orderItem(혹은 Delivery) 객체 생성하고, order만 persist하면 자동으로 orderItem들(혹은 Delivery)이 persist됨.
     삭제 시에도 함께 지워진다.
     주의할 점!!
     다른 엔티티에서도 OrderItem을 참조한다면 cascade를 지정하는데에 신중해야함.
     Order를 지워서 OrderItem이 지워지고, 이것을 참조하는 다른 엔티티도 지워질 수 있기 때문에
    그럴 경우, OrderItem(혹은 Delivery)도 별도의 Repository를 생성하여 persist하는 것이 좋은 선택임.
*/