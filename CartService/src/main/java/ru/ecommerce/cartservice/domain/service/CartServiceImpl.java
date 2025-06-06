package ru.ecommerce.cartservice.domain.service;

import lombok.AllArgsConstructor;
import ru.ecommerce.cartservice.domain.exception.CartNotFoundException;
import ru.ecommerce.cartservice.domain.model.Cart;
import ru.ecommerce.cartservice.domain.model.CartItem;
import ru.ecommerce.cartservice.domain.repository.CartRepository;

import java.util.UUID;


public class CartServiceImpl implements CartService {

    private CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart getOrCreateCart(UUID userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> cartRepository.save(new Cart(userId)));
    }

    @Override
    public Cart addItemToCart(UUID userId, CartItem item) {
        Cart cart = getOrCreateCart(userId);
        cart.addItem(item);
        return cartRepository.save(cart);
    }

    @Override
    public Cart updateItemQuantity(UUID userId, UUID productId, int quantity) {
        Cart cart = getCartForUser(userId);
        cart.updateItemQuantity(productId, quantity);
        return cartRepository.save(cart);
    }

    @Override
    public Cart removeItemFromCart(UUID userId, UUID productId) {
        Cart cart = getCartForUser(userId);
        cart.removeItem(productId);
        return cartRepository.save(cart);
    }

    @Override
    public void clearCart(UUID userId) {
        Cart cart = getCartForUser(userId);
        cart.clear();
        cartRepository.save(cart);
    }

    private Cart getCartForUser(UUID userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart with id: " + " not found"));
    }
}
