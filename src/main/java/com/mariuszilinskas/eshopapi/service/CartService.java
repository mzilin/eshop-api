package com.mariuszilinskas.eshopapi.service;

import com.mariuszilinskas.eshopapi.dto.CartItemDTO;
import com.mariuszilinskas.eshopapi.dto.CartResponse;
import com.mariuszilinskas.eshopapi.dto.CheckoutResponse;

import java.util.List;

public interface CartService {

    CartResponse createCart();
    List<CartResponse> getAllCarts();
    CartResponse updateCart(int cartId, List<CartItemDTO> products);
    CheckoutResponse checkoutCart(int cartId);

}
