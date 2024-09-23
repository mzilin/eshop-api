package com.mariuszilinskas.eshopapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mariuszilinskas.eshopapi.EshopApiApplication;
import com.mariuszilinskas.eshopapi.dto.ProductRequest;
import com.mariuszilinskas.eshopapi.dto.ProductResponse;
import com.mariuszilinskas.eshopapi.enums.Label;
import com.mariuszilinskas.eshopapi.model.Product;
import com.mariuszilinskas.eshopapi.service.ProductService;
import com.mariuszilinskas.eshopapi.util.AppUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EshopApiApplication.class)
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private final int productId = 123;
    private final Product product = new Product();
    private final Product product2 = new Product();
    private ProductRequest productRequest;
    private ProductResponse productResponse;
    private final ObjectMapper objectMapper = new ObjectMapper();

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

        productResponse = new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                AppUtils.convertToDate(product.getAddedAt()),
                AppUtils.convertEnumsToStrings(product.getLabels())
        );
    }

    // ------------------------------------

    @Test
    public void testCreateProduct() throws Exception {
        // Arrange
        when(productService.createProduct(any(ProductRequest.class))).thenReturn(productResponse);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.product_id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.price").value(product.getPrice()))
                .andExpect(jsonPath("$.labels[0]").value("drink"))
                .andExpect(jsonPath("$.labels[1]").value("food"));

        verify(productService, times(1)).createProduct(any(ProductRequest.class));
    }

    // ------------------------------------

    @Test
    public void testGetAllProducts() throws Exception {
        // Arrange
        List<ProductResponse> productList = List.of(
                productResponse,
                new ProductResponse(
                        product2.getId(),
                        product2.getName(),
                        product2.getPrice(),
                        AppUtils.convertToDate(product2.getAddedAt()),
                        AppUtils.convertEnumsToStrings(product2.getLabels())
                )
        );

        when(productService.getAllProducts()).thenReturn(productList);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].product_id").value(product.getId()))
                .andExpect(jsonPath("$[0].name").value(product.getName()))
                .andExpect(jsonPath("$[1].product_id").value(product2.getId()))
                .andExpect(jsonPath("$[1].name").value(product2.getName()));

        verify(productService, times(1)).getAllProducts();
    }

    // ------------------------------------

    @Test
    public void testGetProduct() throws Exception {
        // Arrange
        when(productService.getProduct(productId)).thenReturn(productResponse);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/products/" + productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product_id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.price").value(product.getPrice()))
                .andExpect(jsonPath("$.labels[0]").value("drink"))
                .andExpect(jsonPath("$.labels[1]").value("food"));

        verify(productService, times(1)).getProduct(productId);
    }

    // ------------------------------------

    @Test
    public void testDeleteProduct() throws Exception {
        // Arrange
        doNothing().when(productService).deleteProduct(productId);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/" + productId))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(productId);
    }

}
