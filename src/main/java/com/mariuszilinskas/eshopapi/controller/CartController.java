package com.mariuszilinskas.eshopapi.controller;

import com.mariuszilinskas.eshopapi.dto.CartItemDTO;
import com.mariuszilinskas.eshopapi.dto.CartResponse;
import com.mariuszilinskas.eshopapi.dto.CheckoutResponse;
import com.mariuszilinskas.eshopapi.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartResponse> createCart() {
        CartResponse response = cartService.createCart();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CartResponse>> getAllCarts() {
        List<CartResponse> response = cartService.getAllCarts();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<CartResponse> updateCart(
            @PathVariable int cartId,
            @Valid @RequestBody List<CartItemDTO> products
    ) {
        CartResponse response = cartService.updateCart(cartId, products);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{cartId}/checkout")
    public ResponseEntity<CheckoutResponse> checkoutCart(@PathVariable int cartId) {
        CheckoutResponse response = cartService.checkoutCart(cartId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
