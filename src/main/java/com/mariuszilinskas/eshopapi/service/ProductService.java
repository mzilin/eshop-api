package com.mariuszilinskas.eshopapi.service;

import com.mariuszilinskas.eshopapi.dto.ProductRequest;
import com.mariuszilinskas.eshopapi.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();
    Product getProduct(int productId);
    Product createProduct(ProductRequest request);
    void deleteProduct(int productId);

}
