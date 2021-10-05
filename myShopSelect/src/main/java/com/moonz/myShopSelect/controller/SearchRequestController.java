package com.moonz.myShopSelect.controller;

import com.moonz.myShopSelect.models.ItemDto;
import com.moonz.myShopSelect.utils.NaverShopSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//키워드로 상품 검색하고 그 결과를 목록으로 보여주기
@RequiredArgsConstructor    //final로 선언된 클래스를 자동 생성
@RestController     //JSON으로 응답함을 선언
public class SearchRequestController {
    private final NaverShopSearch naverShopSearch;

    @GetMapping("/api/search")  //api/search?query=검색어 : 주소는 ? 앞에까지. ?query={검색어} 일 경우 @RequestParam 추가
    public List<ItemDto> execSearch(@RequestParam String query) {    // 매개변수 이름과 주소에 들어갈 이름은 일치해야함.
        String resultString = naverShopSearch.search(query);
        return naverShopSearch.fromJsonToItems(resultString);
    }
}
