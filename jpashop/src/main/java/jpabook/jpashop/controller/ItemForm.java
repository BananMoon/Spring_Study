package jpabook.jpashop.controller;

import jpabook.jpashop.common.ItemType;
import jpabook.jpashop.domain.item.Album;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.item.Movie;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
@NoArgsConstructor
@Getter@Setter  // createItemForm.html에서 접근 가능
public class ItemForm {
    // TODO: 2022-06-22 어떤 아이템 종류인지 넣어줘야하지 않나?
    private Long id;    // 상품 수정이 있으므로 필요
    private String name;
    private int price;
    private int stockQuantity;

    @Enumerated(EnumType.STRING)
    private ItemType itemType; // String->ItemType

    private String author;
    private String isbn;

    private String artist;
    private String etc;

    private String director;
    private String actor;

    public Item toEntity() {
        return Book.createBook(author, isbn, name, price, stockQuantity);
/*
        switch(itemType) {
            case ALBUM : return Album.createAlbum(artist, etc, name, price, stockQuantity);
            case BOOK: return Book.createBook(author, isbn, name, price, stockQuantity);
            case MOVIE: return Movie.createMovie(director, actor, name, price, stockQuantity);
        }
*/
        // 에러 발생!
//        throw new UnsupportedOperationException("해당하는 상품이 존재하지 않습니다.");
    }
    @Builder
    ItemForm(Long id, String name, int price, int stockQuantity, ItemType itemType,
             String author, String isbn,String artist,String etc,  String director, String actor){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.itemType = itemType;
        this.author = author;
        this.isbn = isbn;
        this.artist = artist;
        this.etc = etc;
        this.director=director;
        this.actor=actor;
    }
}
