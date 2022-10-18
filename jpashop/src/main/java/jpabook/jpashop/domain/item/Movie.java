package jpabook.jpashop.domain.item;

import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("movie")
@Getter
public class Movie extends Item {

    private String director;

    private String actor;

    private Movie() {}
    private Movie(String director, String actor, String name, int price, int stockQuantity) {
        super();
        this.director = director;
        this.actor = actor;
    }
    public static Movie createMovie(String director, String actor, String name, int price, int stockQuantity) {
        return new Movie(director, actor, name, price, stockQuantity);
    }
}
