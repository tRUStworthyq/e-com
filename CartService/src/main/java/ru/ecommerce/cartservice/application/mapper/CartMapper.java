package ru.ecommerce.cartservice.application.mapper;

import ru.ecommerce.cartservice.application.dto.CartDto;
import ru.ecommerce.cartservice.application.dto.CartItemDto;
import ru.ecommerce.cartservice.domain.model.Cart;
import ru.ecommerce.cartservice.domain.model.CartItem;

public class CartMapper {

    public static CartItemDto toDto(CartItem item) {
        return new CartItemDto(
                item.productId(),
                item.name(),
                item.price(),
                item.quantity()
        );
    }

    public static CartDto toDto(Cart cart) {
        return new CartDto(
                cart.getItems().stream()
                        .map(CartMapper::toDto)
                        .toList()
        );
    }

    public static CartItem toCartItem(CartItemDto itemDto) {
        return new CartItem(
                itemDto.productId(),
                itemDto.name(),
                itemDto.price(),
                itemDto.quantity()
        );
    }
}
