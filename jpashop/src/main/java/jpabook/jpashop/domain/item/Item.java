package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//상속관계 전략 : single table : 한 테이블에 하위 테이블들도 함께
// ALBUM BOOK MOVIE 구현체가 있을 것이므로 abstract
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
@Getter
@Setter
public abstract class Item {
    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

//    @OneToMany(mappedBy = "item")
//    private List<OrderItem> orderItems = new ArrayList<>();

    private String name;
    private int price;
    private int stockQuantity;

    // 객체를 생성하고, persist(영속화)하고나면 하이버네이트는 이를 자신이 관리할 수 있는 class로 바꿔버리기 때문에
    // 이후에 최대한 이 필드를 바꾸지 마라.
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<Category>();

    //==비즈니스 로직==//
    Item(String name, int price, int stockQuantity){
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    protected Item() {

    }
    // DDD: 엔티티 자체가 해결해야하는 작업은 엔티티 내에 비즈니스 로직으로 작성하는 것이 좋다.
    /**
     * stock 증가
     * JPA 장점!! 비즈니스 로직에서 sql문을 작성하지 않아도,
     * 데이터 필드만 바꾸어줘도 JPA가 더티체킹을 통해 db에 update query가 실행된다.
     */
    public void addStock(int quantity) {this.stockQuantity += quantity;}
    /**
     * stock 감소
     */
    public void reduceStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) throw new NotEnoughStockException("need more stock");
        this.stockQuantity -= quantity;
    }

    public void update(int price, String name, int stockQuantity) {
        this.price = price;
        this.name = name;
        this.stockQuantity = stockQuantity;
    }

}
