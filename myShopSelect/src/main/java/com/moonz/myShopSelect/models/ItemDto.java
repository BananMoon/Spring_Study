package com.moonz.myShopSelect.models;

import lombok.Getter;
import org.json.JSONObject;
//NaverShopSearch 클래스를 통해 가져온 정보(String)를 클라이언트에게 돌려주기위한 도구

// Setter는 필요없냐? 아래에서 생성자를 생성해줄 것이기 때문에 없어도 됨
@Getter
public class ItemDto {
    //title, link, image, lprice
    private String title;
    private String link;
    private String image;
    private int lprice;

    public ItemDto(JSONObject itemJson) {
        this.title = itemJson.getString("title");
        this.image = itemJson.getString("images");
        this.link = itemJson.getString("link");
        this.lprice = itemJson.getInt("lprice");
    }
}
