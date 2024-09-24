package com.mariuszilinskas.eshopapi.service;

import com.mariuszilinskas.eshopapi.dto.CartItemDTO;
import com.mariuszilinskas.eshopapi.dto.CartResponse;
import com.mariuszilinskas.eshopapi.dto.CheckoutResponse;
import com.mariuszilinskas.eshopapi.dto.UpdateCartRequest;
import com.mariuszilinskas.eshopapi.model.Cart;
import com.mariuszilinskas.eshopapi.model.CartItem;
import com.mariuszilinskas.eshopapi.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
    private final CartRepository cartRepository;

    @Override
    @Transactional
    public CartResponse createCart() {
        logger.info("Creating a new Cart");
        Cart newCart = cartRepository.save(new Cart());
        return mapCartToResponse(newCart);
    }

    @Override
    public List<CartResponse> getAllCarts() {
        return null;
    }

    @Override
    @Transactional
    public CartResponse modifyCart(int cartId, UpdateCartRequest request) {
        return null;
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
                item.getId(),
                item.getQuantity()
        );
    }

    @Override
    @Transactional
    public CheckoutResponse checkoutCart(int cartId) {
        return null;
    }

}
