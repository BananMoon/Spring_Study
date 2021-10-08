package com.moonz.myShopSelect.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity //DB 테이블 역할
@NoArgsConstructor  //기본 생성자
public class Product extends Timestamped{
    @GeneratedValue(strategy = GenerationType.AUTO) //ID 자동 생성 및 증가
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private int lprice;

    @Column(nullable = false)
    private int myprice;

    @Column(nullable = false)
    private String link;

    public Product(ProductRequestDto productRequestDto) {
        this.image = productRequestDto.getImage();
        this.link = productRequestDto.getLink();
        this.lprice = productRequestDto.getLprice();
        this.title = productRequestDto.getTitle();
        this.myprice = 0;   //사용자의 최저가보다 가격이 작으면 최저가 딱지가 붙기 때문에, 사용자가 설정하기 전까지는 0
    }

    public void updateMyPrice(ProductMypriceRequestDto productMypriceRequestDto) {
        this.myprice = productMypriceRequestDto.getMyprice();
    }

    public void updateByItemDto(ItemDto itemDto) {
        this.lprice = itemDto.getLprice();
    }
}