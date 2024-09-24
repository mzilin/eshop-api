package com.mariuszilinskas.eshopapi.dto;

import java.util.List;

public record CartResponse(
        int cart_id,
        List<CartItemDTO> products,
        boolean checked_out
) {}
