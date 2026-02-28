package ru.ecommerce.orderservice;

import org.junit.jupiter.api.Test;
import ru.ecommerce.orderservice.application.dto.request.CartItem;
import ru.ecommerce.orderservice.domain.exception.DomainException;
import ru.ecommerce.orderservice.domain.model.Order;
import ru.ecommerce.orderservice.domain.model.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTests {
    private final UUID userId = UUID.randomUUID();
    private final CartItem validItem = new CartItem(
            UUID.randomUUID(), "Product", BigDecimal.valueOf(10.0), 2
    );


    @Test
    void createOrder_WithValidItems_ShouldCreateOrder() {
        List<CartItem> cartItems = List.of(validItem);

        Order order = Order.create(userId, cartItems);

        assertNotNull(order.getId());
        assertEquals(userId, order.getUserId());
        assertEquals(1, order.getOrderItems().size());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertNotNull(order.getCreatedAt());
    }

    @Test
    void createOrder_WithNegativePrice_ShouldThrowException() {
        CartItem invalidItem = new CartItem(
                UUID.randomUUID(), "Product", BigDecimal.valueOf(-5.0), 1
        );
        List<CartItem> cartItems = List.of(invalidItem);

        DomainException exception = assertThrows(DomainException.class,
                () -> Order.create(userId, cartItems));
        assertEquals("Price cannot be negative", exception.getMessage());
    }

    @Test
    void createOrder_WithZeroQuantity_ShouldThrowException() {
        CartItem invalidItem = new CartItem(
                UUID.randomUUID(), "Product", BigDecimal.TEN, 0
        );
        List<CartItem> cartItems = List.of(invalidItem);

        DomainException exception = assertThrows(DomainException.class,
                () -> Order.create(userId, cartItems));
        assertEquals("Quantity cannot be a non positive", exception.getMessage());
    }

    @Test
    void createOrder_WithMultipleItems_ShouldCalculateTotal() {
        CartItem item1 = new CartItem(UUID.randomUUID(), "A", BigDecimal.valueOf(5), 2);
        CartItem item2 = new CartItem(UUID.randomUUID(), "B", BigDecimal.valueOf(7.5), 4);
        List<CartItem> cartItems = List.of(item1, item2);

        Order order = Order.create(userId, cartItems);

        assertEquals(BigDecimal.valueOf(40.0), order.getPrice());
    }

    @Test
    void cancel_ShouldChangeStatusToCanceled() {
        Order order = Order.create(userId, List.of(validItem));

        boolean result = order.cancel();

        assertTrue(result);
        assertEquals(OrderStatus.CANCELED, order.getStatus());
    }

    @Test
    void acceptOrder_ShouldChangeStatusToCreated() {
        Order order = Order.create(userId, List.of(validItem));

        boolean result = order.acceptOrder();

        assertTrue(result);
        assertEquals(OrderStatus.CREATED, order.getStatus());
    }
}

