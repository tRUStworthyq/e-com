package ru.ecommerce.cartservice.application.service;

import ru.ecommerce.cartservice.domain.model.Cart;
import ru.ecommerce.cartservice.domain.model.CartItem;

import java.util.UUID;

public interface AppCartService {
    Cart getOrCreateCart(UUID userId);
    Cart addItemToCart(UUID userId, CartItem item);
    Cart updateItemQuantity(UUID userId, UUID productId, int quantity);
    Cart removeItemFromCart(UUID userId, UUID productId);
    void clearCart(UUID userId);
}
