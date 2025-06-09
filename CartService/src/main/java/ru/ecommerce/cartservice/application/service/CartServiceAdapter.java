package ru.ecommerce.cartservice.application.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ecommerce.cartservice.domain.model.Cart;
import ru.ecommerce.cartservice.domain.model.CartItem;
import ru.ecommerce.cartservice.domain.service.CartService;

import java.util.UUID;

@AllArgsConstructor
@Service
public class CartServiceAdapter implements AppCartService {

    @Autowired
    private CartService cartService;

    @Override
    public Cart getOrCreateCart(UUID userId) {
        return cartService.getOrCreateCart(userId);
    }

    @Override
    public Cart addItemToCart(UUID userId, CartItem item) {
        return cartService.addItemToCart(userId, item);
    }

    @Override
    public Cart updateItemQuantity(UUID userId, UUID productId, int quantity) {
        return cartService.updateItemQuantity(userId, productId, quantity);
    }

    @Override
    public Cart removeItemFromCart(UUID userId, UUID productId) {
        return cartService.removeItemFromCart(userId, productId);
    }

    @Override
    public void clearCart(UUID userId) {
        cartService.clearCart(userId);
    }
}
