package com.mariuszilinskas.eshopapi.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponse(
        int product_id,
        String name,
        BigDecimal price,
        String added_at,
        List<String> labels
) {}
