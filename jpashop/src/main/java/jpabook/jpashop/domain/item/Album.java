package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("album")
@Getter
@NoArgsConstructor
public class Album extends Item {

    private String artist;

    private String etc;

    private Album(String artist, String etc, String name, int price, int stockQuantity) {
        super(name, price, stockQuantity);
        this.artist = artist;
        this.etc = etc;
    }
    public static Album createAlbum(String artist, String etc, String name, int price, int stockQuantity) {
        return new Album(artist, etc, name, price, stockQuantity);
    }
}
