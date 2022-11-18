package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
// 조인 테이블
@Entity
@Getter
@NoArgsConstructor
public class OrderItem {
    @Id @GeneratedValue
    @Column(name="order_item_id")
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)  // OrderItem:Order (한 오더에 여러 오더아이템들). order 하나 조회하는데 모든 오더아이템이 조회되면 X
    @JoinColumn(name = "order_id")
    private Order order;    /* db 상 FK이므로 바로 값을 가져온다. */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int orderPrice; // 주문 가격
    private int count;  // 주문 수량

    //==생성 메서드==//
    private OrderItem(Item item, int orderPrice, int count) {
        this.item = item;
        this.orderPrice = orderPrice;
        this.count = count;
    }
    // OrderItem을 먼저 만들고, Order 객체를 만든다.
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem(item, orderPrice, count);
        // Item에도 price가 있지만 할인 등등 예외가 있을 수 있으므로 따로 받는다.

        item.reduceStock(count);
        return orderItem;
    }
    //==비즈니스 로직==//
    public void cancel() {
        item.addStock(this.count);  // getItem()과 item 다른가?
    }
    //==조회 로직==//
    /**
     * 주문상품 전체 가격 조회
     */
    public int getItemsPrice() {return getOrderPrice() * getCount();}
}
