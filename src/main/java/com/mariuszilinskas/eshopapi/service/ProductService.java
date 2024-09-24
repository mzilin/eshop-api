package com.mariuszilinskas.eshopapi.service;

import com.mariuszilinskas.eshopapi.dto.ProductRequest;
import com.mariuszilinskas.eshopapi.dto.ProductResponse;
import com.mariuszilinskas.eshopapi.model.Product;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);
    List<ProductResponse> getAllProducts();
    ProductResponse getProduct(int productId);
    void deleteProduct(int productId);
    Product findProductById(int id);

}
