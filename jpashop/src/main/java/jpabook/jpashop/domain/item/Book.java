package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("book")
@Getter
@RequiredArgsConstructor
public class Book extends Item {
    private String author;
    private String isbn;

    private Book (String author, String isbn, String name, int price, int stockQuantity) {
        super(name, price, stockQuantity);
        this.author= author;
        this.isbn = isbn;
    }

    public static Book createBook(String author, String isbn, String name, int price, int stockQuantity) {
        return new Book(author, isbn, name, price, stockQuantity);
    }
}
