package com.moonz.myShopSelect.service;

import com.moonz.myShopSelect.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class ProductService {
    // 서비스에 항상 필요한 녀석들은 final 키워드와 Spring에게 생성 요청을 위해 @RequiredArgsConstructor
    private final ProductRepository productRepository;

    @Transactional//DB정보를 업데이트해주는 메서드이다.
    public Long updateMyPrice(Long id, ProductMypriceRequestDto productMypriceRequestDto) {
        Product product = productRepository.findById(id).orElseThrow(
                ()-> new NullPointerException("아이디가 존재하지 않습니다.")
        );
        product.updateMyPrice(productMypriceRequestDto);
        return id;
    }

    @Transactional  //db 업데이트해야하므로
    public Long updateBySearch(Long id, ItemDto itemDto){
        Product product = productRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        product.updateByItemDto(itemDto);
        return id;
    }
}