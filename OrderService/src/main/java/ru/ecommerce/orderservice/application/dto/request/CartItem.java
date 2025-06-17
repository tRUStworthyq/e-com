package ru.ecommerce.orderservice.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Элемент корзины для создания заказа")
public record CartItem(
        @Schema(
                description = "UUID товара",
                example = "b1b8e7f5-1ad4-4f27-90ec-7ed906558478",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        UUID productId,
        @Schema(
                description = "Название товара",
                example = "Ноутбук HP",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String name,
        @Schema(
                description = "Цена за единицу товара",
                example = "29999.99",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        BigDecimal price,
        @Schema(
                description = "Количество товара",
                example = "2",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minimum = "1"
        )
        int quantity
) {
}
