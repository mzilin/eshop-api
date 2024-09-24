package com.mariuszilinskas.eshopapi.service;

import com.mariuszilinskas.eshopapi.dto.*;
import com.mariuszilinskas.eshopapi.exception.CheckedOutCartException;
import com.mariuszilinskas.eshopapi.exception.ResourceNotFoundException;
import com.mariuszilinskas.eshopapi.model.Cart;
import com.mariuszilinskas.eshopapi.model.CartItem;
import com.mariuszilinskas.eshopapi.model.Product;
import com.mariuszilinskas.eshopapi.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
    private final CartRepository cartRepository;
    private final ProductService productService;

    @Override
    @Transactional
    public CartResponse createCart() {
        logger.info("Creating a new Cart");
        Cart newCart = cartRepository.save(new Cart());
        return mapCartToResponse(newCart);
    }

    @Override
    public List<CartResponse> getAllCarts() {
        logger.info("Getting all Carts");
        List<Cart> carts = cartRepository.findAll();
        return carts.stream()
                .map(this::mapCartToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CartResponse updateCart(int cartId, List<CartItemDTO> products) {
        logger.info("Updating Cart with id: '{}'", cartId);

        Cart cart = findCartById(cartId);
        checkCartNotCheckedOut(cart);
        Set<Integer> requestedProductIds = extractProductIdsFromRequest(products);
        removeAbsentCartItems(cart, requestedProductIds);
        addOrUpdateCartItems(cart, products);

        return mapCartToResponse(cartRepository.save(cart));
    }

    private void checkCartNotCheckedOut(Cart cart) {
        if (cart.isCheckedOut()) {
            throw new CheckedOutCartException();
        }
    }

    private Set<Integer> extractProductIdsFromRequest(List<CartItemDTO> products) {
        return products.stream()
                .map(CartItemDTO::product_id)
                .collect(Collectors.toSet());
    }

    private void removeAbsentCartItems(Cart cart, Set<Integer> requestedProductIds) {
        cart.getProducts().removeIf(cartItem ->
                !requestedProductIds.contains(cartItem.getProduct().getId())
        );
    }

    private void addOrUpdateCartItems(Cart cart, List<CartItemDTO> products) {
        products.forEach(productItem -> {
            Product product = productService.findProductById(productItem.product_id());
            CartItem cartItem = findOrCreateCartItem(cart, product);
            cartItem.setQuantity(productItem.quantity());
        });
    }

    private CartItem findOrCreateCartItem(Cart cart, Product product) {
        return cart.getProducts().stream()
                .filter(item -> item.getProduct().getId() == product.getId())
                .findFirst()
                .orElseGet(() -> addNewCartItem(cart, product));
    }

    private CartItem addNewCartItem(Cart cart, Product product) {
        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        cart.getProducts().add(newCartItem);
        return newCartItem;
    }

    private CartResponse mapCartToResponse(Cart cart) {
        return new CartResponse(
                cart.getId(),
                cart.getProducts().stream()
                        .map(this::mapCartItemToResponse)
                        .collect(Collectors.toList()),
                cart.isCheckedOut()
        );
    }

    private CartItemDTO mapCartItemToResponse(CartItem item) {
        return new CartItemDTO(
                item.getProduct().getId(),
                item.getQuantity()
        );
    }

    @Override
    @Transactional
    public CheckoutResponse checkoutCart(int cartId) {
        return null;
    }

    private Cart findCartById(int cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(Cart.class, "cart_id", cartId));
    }

}
