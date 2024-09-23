package com.mariuszilinskas.eshopapi.service;

import com.mariuszilinskas.eshopapi.dto.ProductRequest;
import com.mariuszilinskas.eshopapi.dto.ProductResponse;
import com.mariuszilinskas.eshopapi.enums.Label;
import com.mariuszilinskas.eshopapi.exception.EntityExistsException;
import com.mariuszilinskas.eshopapi.model.Product;
import com.mariuszilinskas.eshopapi.repository.ProductRepository;
import com.mariuszilinskas.eshopapi.util.AppUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;

    @Override
    public List<ProductResponse> getAllProducts() {
        logger.info("Getting all Products");
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::mapProductToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse getProduct(int productId) {
        return null;
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        logger.info("Creating new Product: '{}'", request.name());
        checkProductNameExists(request.name());
        Product newProduct = populateNewProductWithRequestData(request);
        return mapProductToResponse(newProduct);
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
        product.setLabels(AppUtils.convertStringsToEnums(request.labels(), Label.class));
        return productRepository.save(product);
    }

    private ProductResponse mapProductToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                AppUtils.convertToDate(product.getAddedAt()),
                AppUtils.convertEnumsToStrings(product.getLabels())
        );
    }

    @Override
    public void deleteProduct(int productId) {

    }

}
