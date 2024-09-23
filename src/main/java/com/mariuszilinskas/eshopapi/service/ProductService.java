package com.mariuszilinskas.eshopapi.service;

import com.mariuszilinskas.eshopapi.dto.ProductRequest;
import com.mariuszilinskas.eshopapi.dto.ProductResponse;

import java.util.List;

public interface ProductService {

    List<ProductResponse> getAllProducts();
    ProductResponse getProduct(int productId);
    ProductResponse createProduct(ProductRequest request);
    void deleteProduct(int productId);

}
