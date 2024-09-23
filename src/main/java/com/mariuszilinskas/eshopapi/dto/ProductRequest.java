package com.mariuszilinskas.eshopapi.dto;

import com.mariuszilinskas.eshopapi.enums.Label;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record ProductRequest(

        @NotBlank(message = "name cannot be blank")
        @Size(max = 200, message = "name cannot exceed 200 characters")
        String name,
        BigDecimal price,
        List<Label> labels
) {}
