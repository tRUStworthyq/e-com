package ru.ecommerce.orderservice.domain.model;


import ru.ecommerce.orderservice.application.dto.request.CartItem;
import ru.ecommerce.orderservice.domain.exception.DomainException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class Order {
    private UUID id;
    private UUID userId;
    private List<OrderItem> orderItems;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private OrderStatus status;

    public Order(UUID id, UUID userId, List<OrderItem> orderItems, BigDecimal price) {
        this.id = id;
        this.userId = userId;
        this.orderItems = orderItems;
        this.price = price;
        this.createdAt = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
    }

    public static Order create(UUID userId, List<CartItem> cartItems) {
        cartItems.forEach(Order::validateCart);
        List<OrderItem> items = convertCartItems(cartItems);
        BigDecimal sumPrice = findSumPrice(cartItems);
        return new Order(UUID.randomUUID(), userId, items, sumPrice);
    }

    public boolean cancel() {
        this.status = OrderStatus.CANCELED;
        return true;
    }

    public boolean acceptOrder() {
        this.status = OrderStatus.CREATED;
        return true;
    }

    private static void validateCart(CartItem cartItem) {
        if (cartItem.price().compareTo(BigDecimal.ZERO) < 0 || cartItem.quantity() <= 0) {
            if (cartItem.price().compareTo(BigDecimal.ZERO) < 0) {
                throw new DomainException("Price cannot be negative");
            }
            if (cartItem.quantity() <= 0) {
                throw new DomainException("Quantity cannot be a non positive");
            }
        }
    }

    private static List<OrderItem> convertCartItems(List<CartItem> cartItems) {
        return cartItems.stream().map(cartItem -> new OrderItem(cartItem.productId(), cartItem.name(), cartItem.price(), cartItem.quantity())).toList();
    }

    private static BigDecimal findSumPrice(List<CartItem> cartItems) {
        Optional<BigDecimal> sum = cartItems.stream().map(CartItem::price).reduce(BigDecimal::add);
        if (sum.isPresent()) {
            return sum.get();
        } else {
            throw new DomainException("Sum price is null");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Objects.equals(id, order.id) && Objects.equals(userId, order.userId) && Objects.equals(orderItems, order.orderItems) && Objects.equals(price, order.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, orderItems, price);
    }

    private Order() {
    }

    public UUID getId()  {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
