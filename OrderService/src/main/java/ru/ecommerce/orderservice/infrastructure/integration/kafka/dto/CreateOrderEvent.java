package ru.ecommerce.orderservice.infrastructure.integration.kafka.dto;

import java.util.UUID;

public record CreateOrderEvent(
        UUID orderId,
        UUID userId
) {
}
