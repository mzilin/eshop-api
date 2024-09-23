package com.mariuszilinskas.eshopapi.service;

import com.mariuszilinskas.eshopapi.dto.ProductRequest;
import com.mariuszilinskas.eshopapi.exception.EntityExistsException;
import com.mariuszilinskas.eshopapi.model.Product;
import com.mariuszilinskas.eshopapi.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return null;
    }

    @Override
    public Product getProduct(int productId) {
        return null;
    }

    @Override
    @Transactional
    public Product createProduct(ProductRequest request) {
        logger.info("Creating new Product: '{}'", request.name());
        checkProductNameExists(request.name());
        return populateNewProductWithRequestData(request);
    }

    private void checkProductNameExists(String name) {
        if (productRepository.existsByName(name)) {
            throw new EntityExistsException(Product.class, "name", name);
        }
    }

    private Product populateNewProductWithRequestData(ProductRequest request) {
        Product product = new Product();
        product.setName(request.name());
        product.setPrice(request.price());
        product.setLabels(request.labels());
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(int productId) {

    }

}
