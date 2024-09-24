package com.mariuszilinskas.eshopapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateCartRequest(

        @NotNull(message = "Products list cannot be null")
        @Valid
        List<CartItemDTO> products

) {}
