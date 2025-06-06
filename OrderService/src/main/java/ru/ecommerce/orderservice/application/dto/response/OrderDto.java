package ru.ecommerce.orderservice.application.dto.response;

import ru.ecommerce.orderservice.domain.model.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderDto(
        UUID id,
        List<OrderItem> items,
        BigDecimal price,
        LocalDateTime createdAt
) {
}
