package ru.ecommerce.orderservice.domain.model;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItem(
        UUID productId,
        String name,
        BigDecimal price,
        int quantity
) {
}
