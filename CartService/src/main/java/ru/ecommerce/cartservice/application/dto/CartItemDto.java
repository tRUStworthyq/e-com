package ru.ecommerce.cartservice.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItemDto(
        UUID productId,
        String name,
        BigDecimal price,
        int quantity
) {
}
