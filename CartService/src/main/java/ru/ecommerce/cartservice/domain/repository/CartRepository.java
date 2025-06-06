package ru.ecommerce.cartservice.domain.repository;

import ru.ecommerce.cartservice.domain.model.Cart;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository {
    Optional<Cart> findById(UUID cartId);
    Optional<Cart> findByUserId(UUID userId);
    Cart save(Cart cart);
}
