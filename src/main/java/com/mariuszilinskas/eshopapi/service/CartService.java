package com.mariuszilinskas.eshopapi.service;

import com.mariuszilinskas.eshopapi.dto.CartResponse;
import com.mariuszilinskas.eshopapi.dto.CheckoutResponse;
import com.mariuszilinskas.eshopapi.dto.UpdateCartRequest;

import java.util.List;

public interface CartService {

    CartResponse createCart();
    List<CartResponse> getAllCarts();
    CartResponse modifyCart(int cartId, UpdateCartRequest request);
    CheckoutResponse checkoutCart(int cartId);

}
