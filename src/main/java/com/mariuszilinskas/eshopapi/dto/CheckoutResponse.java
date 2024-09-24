package com.mariuszilinskas.eshopapi.dto;

import java.math.BigInteger;

public record CheckoutResponse(
        CartResponse cart,
        BigInteger total_cost
) {}
