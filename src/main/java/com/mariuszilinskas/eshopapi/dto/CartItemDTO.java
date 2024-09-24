package com.mariuszilinskas.eshopapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemDTO(

        @NotNull(message = "product_id cannot be null")
        int product_id,

        @NotNull(message = "quantity cannot be null")
        @Min(value = 1, message = "quantity must be greater than 0")
        int quantity

) {}
