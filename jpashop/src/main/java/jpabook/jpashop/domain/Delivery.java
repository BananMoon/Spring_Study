package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Delivery {
    @Id
    @GeneratedValue
    @Column(name="delivery_id")
    private Long id;

    // 1:1 매핑일 때는 어디에 FK를 둬야할까? 장단점이 존재하지만, 더 자주 조회되는 엔티티에 FK를 놓는다 생각하면 배송보다는 주문에 FK를 둔다.
    // XToOne은 EAGER 타입이 디폴트므로, LAZY로 바꿔야함
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;
    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)    // EnumType.ORDINAL : 정수로 변환되어 추후에 타입이 추가될때 문제발생.
    private DeliveryStatus status;

    void setOrder(Order order) {
        this.order = order;
    }

    public Delivery(Address address, DeliveryStatus deliveryStatus) {
        this.address = address;
        this.status = deliveryStatus;
    }
}
