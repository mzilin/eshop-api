package com.mariuszilinskas.eshopapi.service;

import com.mariuszilinskas.eshopapi.dto.CartItemDTO;
import com.mariuszilinskas.eshopapi.dto.CartResponse;
import com.mariuszilinskas.eshopapi.dto.CheckoutResponse;
import com.mariuszilinskas.eshopapi.enums.Label;
import com.mariuszilinskas.eshopapi.exception.CheckedOutCartException;
import com.mariuszilinskas.eshopapi.exception.ResourceNotFoundException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartServiceImpl cartService;

    private final int cartId = 1;
    private final Cart cart = new Cart();
    private final Cart cart2 = new Cart();
    private final CartItem item1 = new CartItem();
    private final CartItem item2 = new CartItem();
    private final Product product1 = new Product();
    private final Product product2 = new Product();

    // ------------------------------------

    @BeforeEach
    void setUp() {
        cart.setId(cartId);

        product1.setId(1);
        product1.setName("Fancy IPA Beer");
        product1.setPrice(new BigDecimal("5.99"));
        product1.setAddedAt(ZonedDateTime.now());
        product1.setLabels(List.of(Label.DRINK, Label.FOOD));
        item1.setProduct(product1);
        item1.setQuantity(2);

        product2.setId(2);
        product2.setName("Delicious Cake");
        product2.setPrice(new BigDecimal("10.11"));
        product2.setAddedAt(ZonedDateTime.now());
        product2.setLabels(List.of(Label.FOOD));
        item2.setProduct(product2);
        item2.setQuantity(3);

        cart.setProducts(new ArrayList<>(List.of(item1, item2)));
        cart.setCheckedOut(false);

        // -------------

        cart2.setId(2);
        cart2.setProducts(new ArrayList<>(List.of()));
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

    @Test
    void testGetAllCarts_Success() {
        // Arrange
        List<Cart> carts = List.of(cart, cart2);

        when(cartRepository.findAll()).thenReturn(carts);

        // Act
        List<CartResponse> response = cartService.getAllCarts();

        // Assert
        assertNotNull(response);
        assertEquals(carts.size(), response.size());
        assertEquals(cart.getId(), response.get(0).cart_id());
        assertEquals(cart.getProducts().size(), response.get(0).products().size());
        assertEquals(cart2.getId(), response.get(1).cart_id());
        assertEquals(cart2.getProducts().size(), response.get(1).products().size());

        verify(cartRepository, times(1)).findAll();
    }

    // ------------------------------------

    @Test
    void testUpdateCart_AddsProductsToNewCart() {
        // Arrange
        List<CartItemDTO> products = List.of(new CartItemDTO(1, 20), new CartItemDTO(2, 2));
        int id = cart2.getId();

        when(cartRepository.findById(id)).thenReturn(Optional.of(cart2));
        when(productService.findProductById(1)).thenReturn(product1);
        when(productService.findProductById(2)).thenReturn(product2);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart2);

        // Act
        CartResponse response = cartService.updateCart(id, products);

        // Assert
        assertNotNull(response);
        assertEquals(cart2.getId(), response.cart_id());
        assertEquals(2, response.products().size());

        assertEquals(20, response.products().stream().filter(p -> p.product_id() == 1).findFirst().get().quantity());
        assertEquals(2, response.products().stream().filter(p -> p.product_id() == 2).findFirst().get().quantity());

        verify(cartRepository, times(1)).findById(id);
        verify(cartRepository, times(1)).save(cart2);

        verify(productService, times(1)).findProductById(1);
        verify(productService, times(1)).findProductById(2);
    }

    @Test
    void testUpdateCart_UpdatesProductQuantities() {
        // Arrange
        List<CartItemDTO> products = List.of(new CartItemDTO(1, 20), new CartItemDTO(2, 2));

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(productService.findProductById(1)).thenReturn(cart.getProducts().get(0).getProduct());
        when(productService.findProductById(2)).thenReturn(cart.getProducts().get(1).getProduct());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // Act
        CartResponse response = cartService.updateCart(cartId, products);

        // Assert
        assertNotNull(response);
        assertEquals(cart.getId(), response.cart_id());
        assertEquals(2, response.products().size());

        assertEquals(20, response.products().stream().filter(p -> p.product_id() == 1).findFirst().get().quantity());
        assertEquals(2, response.products().stream().filter(p -> p.product_id() == 2).findFirst().get().quantity());


        verify(cartRepository, times(1)).findById(cartId);
        verify(productService, times(1)).findProductById(1);
        verify(productService, times(1)).findProductById(2);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testUpdateCart_RemovesProductsFromCart() {
        // Arrange
        List<CartItemDTO> products = List.of(new CartItemDTO(1, 20));

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(productService.findProductById(1)).thenReturn(cart.getProducts().get(0).getProduct());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // Act
        CartResponse response = cartService.updateCart(cartId, products);

        // Assert
        assertNotNull(response);
        assertEquals(cart.getId(), response.cart_id());
        assertEquals(1, response.products().size());

        assertEquals(20, response.products().stream().filter(p -> p.product_id() == 1).findFirst().get().quantity());

        verify(cartRepository, times(1)).findById(cartId);
        verify(productService, times(1)).findProductById(1);
        verify(productService, never()).findProductById(2);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testUpdateCart_AddsProductsToNonExistentCart() {
        // Arrange
        int nonExistentId = 444;
        List<CartItemDTO> products = List.of(new CartItemDTO(1, 20));
        when(cartRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Assert & Act
        assertThrows(ResourceNotFoundException.class, () -> cartService.updateCart(nonExistentId, products));

        verify(cartRepository, times(1)).findById(nonExistentId);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void testUpdateCart_AddsProductsToCheckedOutCart() {
        // Arrange
        cart.setCheckedOut(true);
        List<CartItemDTO> products = List.of(new CartItemDTO(1, 20));

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        // Assert & Act
        assertThrows(CheckedOutCartException.class, () -> cartService.updateCart(cartId, products));

        verify(cartRepository, times(1)).findById(cartId);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    // ------------------------------------

    @Test
    void testCheckoutCart_Success() {
        // Arrange
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // Act
        CheckoutResponse response = cartService.checkoutCart(cartId);

        // Assert
        assertNotNull(response);
        assertEquals(cart.getId(), response.cart().cart_id());
        assertEquals(cart.getProducts().size(), response.cart().products().size());
        assertTrue(response.cart().checked_out());
        BigDecimal expectedTotalCost = item1.getProduct().getPrice().multiply(BigDecimal.valueOf(item1.getQuantity()))
                .add(item2.getProduct().getPrice().multiply(BigDecimal.valueOf(item2.getQuantity())));
        assertEquals(expectedTotalCost, response.total_cost());

        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testCheckoutCart_CartIsAlreadyCheckedOut() {
        // Arrange
        cart.setCheckedOut(true);
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        // Assert & Act
        assertThrows(CheckedOutCartException.class, () -> cartService.checkoutCart(cartId));

        verify(cartRepository, times(1)).findById(cartId);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void testCheckoutCart_NonExistentCartId() {
        // Arrange
        int nonExistentId = 444;
        when(cartRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Assert & Act
        assertThrows(ResourceNotFoundException.class, () -> cartService.checkoutCart(nonExistentId));

        verify(cartRepository, times(1)).findById(nonExistentId);
        verify(cartRepository, never()).save(any(Cart.class));
    }

}
