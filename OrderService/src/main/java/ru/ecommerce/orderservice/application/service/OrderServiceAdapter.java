package ru.ecommerce.orderservice.application.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecommerce.orderservice.application.dto.request.CartItem;
import ru.ecommerce.orderservice.domain.model.Order;
import ru.ecommerce.orderservice.domain.service.OrderService;
import ru.ecommerce.orderservice.infrastructure.integration.kafka.dto.CreateOrderEvent;

import java.nio.file.AccessDeniedException;
import java.sql.Time;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@AllArgsConstructor
public class OrderServiceAdapter implements AppOrderService {
    private OrderService orderService;
    private KafkaTemplate<String, CreateOrderEvent> kafkaTemplate;

    private static final String CREATE_ORDER_TOPIC = "orders";
    private static final long KAFKA_SEND_TIMEOUT_MS = 3000;
    @Override
    @Transactional
    public Order createOrder(UUID userId, List<CartItem> cartItems) {
        Order order = orderService.createOrder(userId, cartItems);

        sendOrderCreatedEvent(order.getId(), userId);
        return order;
    }

    @Retryable(
            value = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    public void sendOrderCreatedEvent(UUID id, UUID userId) {
        CreateOrderEvent event = new CreateOrderEvent(id, userId);

        try {
            kafkaTemplate.send(CREATE_ORDER_TOPIC, event)
                    .get(KAFKA_SEND_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException("Failed to send CreateOrderEvent", e);
        }
    }

    @Override
    @Transactional
    public boolean cancelOrder(UUID id, UUID userId) throws AccessDeniedException {
        return orderService.cancelOrder(id, userId);
    }

    @Override
    @Transactional
    public List<Order> findOrdersByUserId(UUID userId, Pageable pageable) {
        return orderService.findOrdersByUserId(userId, pageable);
    }

    @Override
    public boolean acceptOrder(UUID orderId) {
        return orderService.acceptOrder(orderId);
    }
}
