package ru.ecommerce.orderservice.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.ecommerce.orderservice.domain.model.Order;
import ru.ecommerce.orderservice.domain.model.OrderStatus;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DataMongoRepository extends MongoRepository<Order, UUID> {
    Page<Order> findByUserIdAndStatus(UUID userId, Pageable pageable, OrderStatus status);
    Optional<Order> findByIdAndStatus(UUID id, OrderStatus status);
}
