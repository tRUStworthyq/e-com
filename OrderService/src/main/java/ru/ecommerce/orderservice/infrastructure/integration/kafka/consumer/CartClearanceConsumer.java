package ru.ecommerce.orderservice.infrastructure.integration.kafka.consumer;

import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.ecommerce.orderservice.application.service.AppOrderService;
import ru.ecommerce.orderservice.infrastructure.integration.kafka.dto.ClearCartEvent;

import java.nio.file.AccessDeniedException;

@Component
@AllArgsConstructor
public class CartClearanceConsumer {

    private AppOrderService orderService;

    @KafkaListener(topics = "cart-clearance", groupId = "order-service-group")
    public void handleClearanceResult(ClearCartEvent result) {
        if (!result.success()) {
            try {
                orderService.cancelOrder(result.orderId(), result.userId());
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
        } else {
            orderService.acceptOrder(result.orderId());
        }
    }
}
