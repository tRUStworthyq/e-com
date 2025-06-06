package ru.ecommerce.cartservice.application.dto;

import java.util.List;

public record CartDto(
         List<CartItemDto> items
) {
}
