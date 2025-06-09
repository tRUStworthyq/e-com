package ru.ecommerce.cartservice.infrastructure.integration.kafka.dto;

import java.util.UUID;

public record ClearCartEvent(
        UUID orderId,
        UUID userId,
        boolean success
) {
}
