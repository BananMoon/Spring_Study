package com.moonz.myShopSelect.utils;

import com.moonz.myShopSelect.models.ItemDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

// fromJsonToItems() : 네이버검색 결과로 나온 String -> Json -> Dto로 변환
//
@Component
public class NaverShopSearch {
    public String search(String query) {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Naver-Client-Id", "djZdA41zjsn41YspwbHK");
        headers.add("X-Naver-Client-Secret", "CNv69GE4zV");
        String body = "";

        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);   //body와 header를 전달
        ResponseEntity<String> responseEntity = rest.exchange("https://openapi.naver.com/v1/search/shop.json?query="+query, HttpMethod.GET, requestEntity, String.class);
        //responseEntity에 응답 결과물이 들어감.

        HttpStatus httpStatus = responseEntity.getStatusCode(); //응답값의 status값은 httpStatus에 저장
        int status = httpStatus.value();    //status : 200
        String response = responseEntity.getBody(); //getBody() : 응답값(의 body값)은 문자열 1개로 response에 저장
        System.out.println("Response status: " + status);
        System.out.println(response);

        return response;
    }


    //자주 사용될 것을 메서드로
    public List<ItemDto> fromJSONtoItems(String result) {
        JSONObject rjson = new JSONObject(result);  //문자열(result) 정보를 JSONObject로 바꾸기
//        System.out.println(rjson);  // items는 JSONArray
        JSONArray items = rjson.getJSONArray("items");  //key값이 items

        //items인 value들인 itemDto를 리스트로 받고자
        List<ItemDto> itemDtoList = new ArrayList<>();

        //for문을 돌면서 JSONArray에 있는 JSONObject를 꺼내야함
        for (int i=0; i<items.length(); i++) {  //json array는 length()
            JSONObject itemJson = items.getJSONObject(i);
//            System.out.println(itemJson);
            ItemDto itemDto = new ItemDto(itemJson);
            itemDtoList.add(itemDto);

            // itemJson에 있는 String을 뽑기 위해 getString()
//            itemDto를 이용해서 전달
//            String title = itemJson.getString("title");
//            String image = itemJson.getString("image");
//            int lprice = itemJson.getInt("lprice");
//            String link = itemJson.getString("link");
        }
        return itemDtoList;
    }
//    public static void main(String[] args) {
//        NaverShopSearch naverShopSearch = new NaverShopSearch();
//        String result = naverShopSearch.search("아이맥");  // 검색 결과
//
//    }
}
//검색결과를 문자열 -> DTO로 바꾸기
//자바에서 json을 다루는데 도와주는 라이브러리 : org.json 패키지
// - JSONObject {}, JSONArray [] 클래스