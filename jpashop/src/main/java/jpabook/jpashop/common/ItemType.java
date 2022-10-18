package jpabook.jpashop.common;

public enum ItemType {
    MOVIE("movie"),
    BOOK("book"),
    ALBUM("album");

    ItemType(String discriminatorValue) {
    }
}
