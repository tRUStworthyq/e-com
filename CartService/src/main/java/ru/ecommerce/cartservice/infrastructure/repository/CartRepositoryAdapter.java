package ru.ecommerce.cartservice.infrastructure.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ecommerce.cartservice.domain.model.Cart;
import ru.ecommerce.cartservice.domain.repository.CartRepository;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Component
public class CartRepositoryAdapter implements CartRepository {


    private DataMongoRepository cartRepository;


    @Override
    public Optional<Cart> findById(UUID cartId) {
        return cartRepository.findById(cartId);
    }

    @Override
    public Optional<Cart> findByUserId(UUID userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }
}
