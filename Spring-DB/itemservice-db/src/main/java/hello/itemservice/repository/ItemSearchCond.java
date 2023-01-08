package hello.itemservice.repository;

import lombok.Data;

/**
 * 아이템 조회 시 필터링 데이터(검색어)
 */
@Data
public class ItemSearchCond {

    private String itemName;
    private Integer maxPrice;

    public ItemSearchCond() {
    }

    public ItemSearchCond(String itemName, Integer maxPrice) {
        this.itemName = itemName;
        this.maxPrice = maxPrice;
    }
}
