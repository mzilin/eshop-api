package com.mariuszilinskas.eshopapi.service;

import com.mariuszilinskas.eshopapi.dto.ProductRequest;
import com.mariuszilinskas.eshopapi.dto.ProductResponse;
import com.mariuszilinskas.eshopapi.enums.Label;
import com.mariuszilinskas.eshopapi.exception.EntityExistsException;
import com.mariuszilinskas.eshopapi.exception.ResourceNotFoundException;
import com.mariuszilinskas.eshopapi.model.Product;
import com.mariuszilinskas.eshopapi.repository.ProductRepository;
import com.mariuszilinskas.eshopapi.util.AppUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private final int productId = 123;
    private final Product product = new Product();
    private final Product product2 = new Product();
    private ProductRequest productRequest;

    // ------------------------------------

    @BeforeEach
    void setUp() {
        product.setId(productId);
        product.setName("Fancy IPA Beer");
        product.setPrice(new BigDecimal("5.99"));
        product.setAddedAt(ZonedDateTime.now());
        product.setLabels(List.of(Label.DRINK, Label.FOOD));

        product2.setId(124);
        product2.setName("Delicious Cake");
        product2.setPrice(new BigDecimal("10.11"));
        product2.setAddedAt(ZonedDateTime.now());
        product2.setLabels(List.of(Label.FOOD));

        productRequest = new ProductRequest(
                product.getName(),
                product.getPrice(),
                AppUtils.convertEnumsToStrings(product.getLabels())
        );
    }

    // ------------------------------------

    @Test
    void testCreateProduct_Success() {
        // Arrange
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);

        when(productRepository.existsByName(productRequest.name())).thenReturn(false);
        when(productRepository.save(captor.capture())).thenReturn(product);

        // Act
        ProductResponse response = productService.createProduct(productRequest);

        // Assert
        assertNotNull(response);
        assertEquals(product.getId(), response.product_id());
        assertEquals(product.getName(), response.name());
        assertEquals(product.getPrice(), response.price());
        assertEquals(AppUtils.convertEnumsToStrings(product.getLabels()), response.labels());

        verify(productRepository, times(1)).existsByName(productRequest.name());
        verify(productRepository, times(1)).save(captor.capture());

        Product savedProduct = captor.getValue();
        assertEquals(productRequest.name(), savedProduct.getName());
        assertEquals(productRequest.price(), savedProduct.getPrice());
        assertEquals(productRequest.labels(), AppUtils.convertEnumsToStrings(savedProduct.getLabels()));
    }

    @Test
    void testCreateProduct_SameProductNameAlreadyExists() {
        // Arrange
        when(productRepository.existsByName(productRequest.name())).thenReturn(true);

        //Act & Assert
        assertThrows(EntityExistsException.class, () -> productService.createProduct(productRequest));

        // Assert
        verify(productRepository, times(1)).existsByName(productRequest.name());
        verify(productRepository, never()).save(any(Product.class));
    }

    // ------------------------------------

    @Test
    void testGetAllProducts() {
        // Arrange
        List<Product> products = List.of(product, product2);

        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<ProductResponse> response = productService.getAllProducts();

        // Assert
        assertNotNull(response);
        assertEquals(products.size(), response.size());
        assertEquals(product.getId(), response.get(0).product_id());
        assertEquals(product.getName(), response.get(0).name());
        assertEquals(product2.getId(), response.get(1).product_id());
        assertEquals(product2.getName(), response.get(1).name());

        verify(productRepository, times(1)).findAll();
    }

    // ------------------------------------

    @Test
    void testGetProduct_Success() {
        // Arrange
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        ProductResponse response = productService.getProduct(productId);

        // Assert
        assertNotNull(response);
        assertEquals(product.getId(), response.product_id());
        assertEquals(product.getName(), response.name());
        assertEquals(product.getPrice(), response.price());
        assertEquals(AppUtils.convertEnumsToStrings(product.getLabels()), response.labels());

        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testGetProduct_NonExistentProductId() {
        // Arrange
        int nonExistentId = 444;
        when(productRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Assert & Act
        assertThrows(ResourceNotFoundException.class, () -> productService.getProduct(nonExistentId));

        verify(productRepository, times(1)).findById(nonExistentId);
    }

    // ------------------------------------


    @Test
    void testDeleteProduct_Success() {
        // Arrange
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);

        // Act
        productService.deleteProduct(productId);

        // Assert
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).delete(product);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        assertFalse(productRepository.findById(productId).isPresent());
    }

    @Test
    void testDeleteProduct_NonExistentProductId() {
        // Arrange
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Assert & Act
        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(productId));

        // Assert
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).delete(any(Product.class));
    }

    // ------------------------------------

    @Test
    void testFindProductById_Success() {
        // Arrange
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        Product response = productService.findProductById(productId);

        // Assert
        assertNotNull(response);
        assertEquals(product.getId(), response.getId());
        assertEquals(product.getName(), response.getName());
        assertEquals(product.getPrice(), response.getPrice());
        assertEquals(product.getLabels(), response.getLabels());

        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testFindProductById_NonExistentProductId() {
        // Arrange
        int nonExistentId = 444;
        when(productRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Assert & Act
        assertThrows(ResourceNotFoundException.class, () -> productService.findProductById(nonExistentId));

        verify(productRepository, times(1)).findById(nonExistentId);
    }
}
