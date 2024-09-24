package com.mariuszilinskas.eshopapi.service;

import com.mariuszilinskas.eshopapi.dto.CartResponse;
import com.mariuszilinskas.eshopapi.enums.Label;
import com.mariuszilinskas.eshopapi.model.Cart;
import com.mariuszilinskas.eshopapi.model.CartItem;
import com.mariuszilinskas.eshopapi.model.Product;
import com.mariuszilinskas.eshopapi.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private final int cartId = 1;
    private final Cart cart = new Cart();
    private final Cart cart2 = new Cart();

    // ------------------------------------

    @BeforeEach
    void setUp() {
        cart.setId(cartId);

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

        cart.setProducts(List.of(item1, item2));
        cart.setCheckedOut(false);

        // -------------

        cart2.setId(2);
        cart2.setProducts(List.of());
        cart2.setCheckedOut(false);
    }

    // ------------------------------------

    @Test
    void testCreateCart_Success() {
        // Arrange
        when(cartRepository.save(any(Cart.class))).thenReturn(cart2);

        // Act
        CartResponse response = cartService.createCart();

        // Assert
        assertNotNull(response);
        assertEquals(cart2.getId(), response.cart_id());
        assertEquals(0, response.products().size());

        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    // ------------------------------------


    // ------------------------------------


    // ------------------------------------


}
