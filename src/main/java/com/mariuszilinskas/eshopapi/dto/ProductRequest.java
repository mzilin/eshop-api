package com.mariuszilinskas.eshopapi.dto;

import com.mariuszilinskas.eshopapi.enums.Label;

import java.math.BigDecimal;
import java.util.List;

public record ProductRequest(
        String name,
        BigDecimal price,
        List<Label> labels
) {}
