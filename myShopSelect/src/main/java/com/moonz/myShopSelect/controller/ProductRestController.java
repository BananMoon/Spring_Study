package com.moonz.myShopSelect.controller;

import com.moonz.myShopSelect.models.Product;
import com.moonz.myShopSelect.models.ProductMypriceRequestDto;
import com.moonz.myShopSelect.models.ProductRepository;
import com.moonz.myShopSelect.models.ProductRequestDto;
import com.moonz.myShopSelect.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor // final로 선언된 멤버 변수를 자동으로 생성합니다.
@RestController // JSON으로 데이터를 주고받음을 선언합니다.
public class ProductRestController {

    private final ProductRepository productRepository;
    private final ProductService productService;

    // 등록된 전체 상품 목록 조회
    @GetMapping("/api/products")
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    // 신규 상품 등록
    @PostMapping("/api/products")
    public Product createProduct(@RequestBody ProductRequestDto requestDto) {
        Product product = new Product(requestDto);
        productRepository.save(product);
        return product;
    }

    //최저가 가격 변경
    //컨트롤러 -> 서비스 -> 엔티티
    @PutMapping("/api/products/{targetId}")
    public Long updateMyPrice(@PathVariable Long targetId, @RequestBody ProductMypriceRequestDto productMypriceRequestDto) {
        return productService.updateMyPrice(targetId, productMypriceRequestDto);
    }
}