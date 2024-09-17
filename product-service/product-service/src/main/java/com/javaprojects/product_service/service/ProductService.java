package com.javaprojects.product_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.javaprojects.product_service.dto.ProductRequest;
import com.javaprojects.product_service.dto.ProductResponse;
import com.javaprojects.product_service.model.Product;
import com.javaprojects.product_service.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    
    private final ProductRepository productRepository;

    public String addProduct(ProductRequest productRequest){
        Product product = Product.builder()
                        .name(productRequest.getName())
                        .description(productRequest.getDescription())
                        .price(productRequest.getPrice())
                        .build();

        productRepository.save(product);
        log.info("Product id :{} is saved"+product.getId());
        return product.getId();
    }

    public List<ProductResponse> getAllProducts(){
        List<Product> products = productRepository.findAll();
        return products.stream()
                        .map(product -> mapToProductResponse(product))
                        .collect(Collectors.toList());
    }

    public ProductResponse mapToProductResponse(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();

    }
}
