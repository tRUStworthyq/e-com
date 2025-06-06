package ru.ecommerce.cartservice.application.dto;

import java.util.UUID;

public record ItemQuantityDto(
        UUID productId,
        int quantity
) {
}
