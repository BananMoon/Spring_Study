package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

// ALBUM BOOK MOVIE 구현체가 있을 것이므로 abstract
@Entity
@Getter
public abstract class Item {
    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

//    @OneToMany(mappedBy = "item")
//    private List<OrderItem> orderItems = new ArrayList<>();

    private String name;

    private int price;

    private int stockQuantity;

    private Lit<Category>
}
