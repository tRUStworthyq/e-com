package ru.ecommerce.orderservice.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.ecommerce.orderservice.domain.model.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findByIdAndStatus(UUID id);
    Page<Order> findByUserId(UUID userId, Pageable pageable);
    Optional<Order> findByIdPendingOrders(UUID id);
    Optional<Order> findById(UUID id);
}
