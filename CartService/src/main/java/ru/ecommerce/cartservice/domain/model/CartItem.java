package ru.ecommerce.cartservice.domain.model;

import java.math.BigDecimal;
import java.util.UUID;


public record CartItem(
        UUID productId,
        String name,
        BigDecimal price,
        int quantity
) {
}
