package ru.ecommerce.cartservice.infrastructure.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.ecommerce.cartservice.domain.model.Cart;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DataMongoRepository extends MongoRepository<Cart, UUID> {
    Optional<Cart> findByUserId(UUID userId);
}
