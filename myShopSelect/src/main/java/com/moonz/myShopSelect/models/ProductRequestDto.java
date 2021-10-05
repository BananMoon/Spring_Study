package com.moonz.myShopSelect.models;

import lombok.Getter;

@Getter
public class ProductRequestDto {
    //title, link, image, price
    private String title;

    private String link;

    private String image;

    private int lprice;
}
