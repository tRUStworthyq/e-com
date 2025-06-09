package ru.ecommerce.cartservice.domain.model;


import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


@Getter
public class Cart {
    private UUID id;
    private UUID userId;
    private List<CartItem> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Cart(UUID userId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.items = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void addItem(CartItem newItem) {
        items.stream()
                .filter(item -> item.productId().equals(newItem.productId()))
                .findFirst()
                .ifPresentOrElse(
                        existingItem -> updateItemQuantity(
                                existingItem.productId(),
                                existingItem.quantity() + newItem.quantity()
                        ),
                        () -> {
                            items.add(newItem);
                            updatedAt = LocalDateTime.now();
                        }
                );
    }

    public void removeItem(UUID productId) {
        boolean removed = items.removeIf(item -> item.productId().equals(productId));
        if (removed) {
            updatedAt = LocalDateTime.now();
        }
    }

    public void updateItemQuantity(UUID productId, int newQuantity) {
        items.stream()
                .filter(item -> item.productId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    items.set(items.indexOf(item), new CartItem(
                            item.productId(),
                            item.name(),
                            item.price(),
                            newQuantity
                    ));
                    updatedAt = LocalDateTime.now();
                });
    }

    public void clear() {
        if (!items.isEmpty()) {
            items.clear();
            updatedAt = LocalDateTime.now();
        }
    }


    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }
}
