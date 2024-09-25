package com.mariuszilinskas.eshopapi.dto;

import java.math.BigDecimal;

public record CheckoutResponse(
        CartResponse cart,
        BigDecimal total_cost
) {}
