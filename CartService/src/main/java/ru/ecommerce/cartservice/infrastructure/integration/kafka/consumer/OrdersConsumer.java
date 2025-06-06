package ru.ecommerce.cartservice.infrastructure.integration.kafka.consumer;

import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import ru.ecommerce.cartservice.application.service.AppCartService;
import ru.ecommerce.cartservice.domain.exception.CartNotFoundException;
import ru.ecommerce.cartservice.infrastructure.integration.kafka.dto.ClearCartEvent;
import ru.ecommerce.cartservice.infrastructure.integration.kafka.dto.CreateOrderEvent;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@AllArgsConstructor
public class OrdersConsumer {

    private AppCartService cartService;
    private KafkaTemplate<String, ClearCartEvent> kafkaTemplate;


    @KafkaListener(topics = "orders", groupId = "order-service-group")
    public void handleCreateOrder(CreateOrderEvent event) {
        try {
            cartService.clearCart(event.userId());
            sendClearCartEvent(event.orderId(), event.userId(), true);
        } catch (CartNotFoundException e) {
            sendClearCartEvent(event.orderId(), event.userId(), false);
            throw new RuntimeException(e);
        }
    }

    @Retryable(
            value = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    private void sendClearCartEvent(UUID orderId, UUID userId, boolean success) {
        ClearCartEvent event = new ClearCartEvent(orderId, userId, success);
        try {
            kafkaTemplate.send("cart-clearance", event)
                    .get(3000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {

        }
    }
}
