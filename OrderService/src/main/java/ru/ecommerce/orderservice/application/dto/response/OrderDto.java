package ru.ecommerce.orderservice.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.ecommerce.orderservice.domain.model.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "Модель заказа")
public record OrderDto(
        @Schema(
                description = "UUID заказа",
                example = "123e4567-e89b-12d3-a456-426614174000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        UUID id,
        @Schema(
                description = "Список товаров в заказе",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        List<OrderItem> items,
        @Schema(
                description = "Общая стоимость заказа",
                example = "59999.98",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        BigDecimal price,
        @Schema(
                description = "Дата и время создания заказа",
                example = "2023-10-05T12:30:45",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        LocalDateTime createdAt
) {
}
