package com.mariuszilinskas.eshopapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mariuszilinskas.eshopapi.EshopApiApplication;
import com.mariuszilinskas.eshopapi.dto.CartItemDTO;
import com.mariuszilinskas.eshopapi.dto.CartResponse;
import com.mariuszilinskas.eshopapi.dto.CheckoutResponse;
import com.mariuszilinskas.eshopapi.enums.Label;
import com.mariuszilinskas.eshopapi.model.Cart;
import com.mariuszilinskas.eshopapi.model.CartItem;
import com.mariuszilinskas.eshopapi.model.Product;
import com.mariuszilinskas.eshopapi.service.CartService;
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
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EshopApiApplication.class)
@AutoConfigureMockMvc
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    private final int cartId = 123;
    private final Cart cart = new Cart();
    private final Cart cart2 = new Cart();
    private CartResponse cartResponse;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // ------------------------------------

    @BeforeEach
    void setUp() {
        CartItem item1 = new CartItem();
        Product product1 = new Product();
        product1.setId(1);
        product1.setName("Fancy IPA Beer");
        product1.setPrice(new BigDecimal("5.99"));
        product1.setAddedAt(ZonedDateTime.now());
        product1.setLabels(List.of(Label.DRINK, Label.FOOD));
        item1.setProduct(product1);
        item1.setQuantity(2);

        CartItem item2 = new CartItem();
        Product product2 = new Product();
        product2.setId(2);
        product2.setName("Delicious Cake");
        product2.setPrice(new BigDecimal("10.11"));
        product2.setAddedAt(ZonedDateTime.now());
        product2.setLabels(List.of(Label.FOOD));
        item2.setProduct(product2);
        item2.setQuantity(3);

        cart.setId(cartId);
        cart.setProducts(new ArrayList<>(List.of(item1, item2)));
        cart.setCheckedOut(false);

        cart2.setId(124);
        cart2.setProducts(new ArrayList<>());
        cart2.setCheckedOut(false);

        List<CartItemDTO> productDTOs = List.of(
                new CartItemDTO(product1.getId(), item1.getQuantity()),
                new CartItemDTO(product2.getId(), item2.getQuantity())
        );

        cartResponse = new CartResponse(
                cart.getId(),
                productDTOs,
                cart.isCheckedOut()
        );
    }


    // ------------------------------------

    @Test
    void testCreateCart() throws Exception {
        // Arrange
        when(cartService.createCart()).thenReturn(cartResponse);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/carts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cart_id").value(cart.getId()))
                .andExpect(jsonPath("$.products").isArray())
                .andExpect(jsonPath("$.checked_out").value(false));

        verify(cartService, times(1)).createCart();
    }

    // ------------------------------------

    @Test
    void testGetAllCarts() throws Exception {
        // Arrange
        List<CartResponse> cartResponses = List.of(cartResponse);
        when(cartService.getAllCarts()).thenReturn(cartResponses);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/carts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cart_id").value(cart.getId()))
                .andExpect(jsonPath("$[0].products").isArray())
                .andExpect(jsonPath("$[0].checked_out").value(false));

        verify(cartService, times(1)).getAllCarts();
    }

    // ------------------------------------

    @Test
    void testUpdateCart() throws Exception {
        // Arrange
        List<CartItemDTO> products = List.of(new CartItemDTO(1, 2), new CartItemDTO(2, 3));
        CartResponse updatedResponse = new CartResponse(cartId, products, false);
        when(cartService.updateCart(eq(cartId), anyList())).thenReturn(updatedResponse);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/carts/{cartId}", cartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(products)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cart_id").value(cartId))
                .andExpect(jsonPath("$.products").isArray())
                .andExpect(jsonPath("$.products[0].product_id").value(1))
                .andExpect(jsonPath("$.products[0].quantity").value(2))
                .andExpect(jsonPath("$.products[1].product_id").value(2))
                .andExpect(jsonPath("$.products[1].quantity").value(3));

        verify(cartService, times(1)).updateCart(eq(cartId), anyList());
    }

    // ------------------------------------

    @Test
    void testCheckoutCart() throws Exception {
        // Arrange
        BigDecimal totalCost = new BigDecimal("50.00");
        CheckoutResponse checkoutResponse = new CheckoutResponse(cartResponse, totalCost);
        when(cartService.checkoutCart(cartId)).thenReturn(checkoutResponse);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/carts/{cartId}/checkout", cartId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cart.cart_id").value(cartId))
                .andExpect(jsonPath("$.cart.checked_out").value(true))
                .andExpect(jsonPath("$.total_cost").value(totalCost));

        verify(cartService, times(1)).checkoutCart(cartId);
    }

}
