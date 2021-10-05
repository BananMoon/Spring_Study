package com.moonz.myShopSelect.service;

import com.moonz.myShopSelect.models.Product;
import com.moonz.myShopSelect.models.ProductMypriceRequestDto;
import com.moonz.myShopSelect.models.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class ProductService {
    // 서비스에 항상 필요한 녀석들은 final 키워드와 Spring에게 생성 요청을 위해 @RequiredArgsConstructor
    private final ProductRepository productRepository;

    @Transactional//DB정보를 업데이트해주는 메서드이다.
    public Long update(Long id, ProductMypriceRequestDto productMypriceRequestDto) {
        Product product = productRepository.findById(id).orElseThrow(
                ()-> new NullPointerException("아이디가 존재하지 않습니다.")
        );
        product.update(productMypriceRequestDto);
        return id;
    }
}