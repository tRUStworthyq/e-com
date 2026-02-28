package ru.ecommerce.cartservice;

import org.junit.jupiter.api.Test;
import ru.ecommerce.cartservice.domain.model.Cart;
import ru.ecommerce.cartservice.domain.model.CartItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CartTests {
    private final UUID userId = UUID.randomUUID();
    private final UUID productId = UUID.randomUUID();

    @Test
    void createCart_ShouldInitializeCorrectly() {
        Cart cart = new Cart(userId);

        assertNotNull(cart.getId());
        assertEquals(userId, cart.getUserId());
        assertTrue(cart.getItems().isEmpty());
        assertNotNull(cart.getCreatedAt());
        assertNotNull(cart.getUpdatedAt());
    }

    @Test
    void addItem_NewItem_ShouldAddToCart() {
        Cart cart = new Cart(userId);
        CartItem item = new CartItem(productId, "Product", BigDecimal.TEN, 1);

        cart.addItem(item);

        assertEquals(1, cart.getItems().size());
        assertEquals(item, cart.getItems().get(0));
    }

    @Test
    void addItem_ExistingItem_ShouldUpdateQuantity() {
        Cart cart = new Cart(userId);
        CartItem initialItem = new CartItem(productId, "Product", BigDecimal.TEN, 1);
        CartItem additionalItem = new CartItem(productId, "Product", BigDecimal.TEN, 2);
        cart.addItem(initialItem);
        LocalDateTime initialUpdate = cart.getUpdatedAt();

        cart.addItem(additionalItem);

        assertEquals(1, cart.getItems().size());
        assertEquals(3, cart.getItems().get(0).quantity());
        assertTrue(cart.getUpdatedAt().isAfter(initialUpdate));
    }

    @Test
    void removeItem_ExistingItem_ShouldRemoveFromCart() {
        Cart cart = new Cart(userId);
        CartItem item = new CartItem(productId, "Product", BigDecimal.TEN, 1);
        cart.addItem(item);
        LocalDateTime initialUpdate = cart.getUpdatedAt();

        cart.removeItem(productId);

        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void removeItem_NonExistingItem_ShouldNotModifyCart() {
        Cart cart = new Cart(userId);
        cart.addItem(new CartItem(productId, "Product", BigDecimal.TEN, 1));
        LocalDateTime initialUpdate = cart.getUpdatedAt();

        cart.removeItem(UUID.randomUUID());

        assertEquals(1, cart.getItems().size());
        assertEquals(initialUpdate, cart.getUpdatedAt());
    }

    @Test
    void updateItemQuantity_ExistingItem_ShouldUpdateQuantity() {
        Cart cart = new Cart(userId);
        CartItem item = new CartItem(productId, "Product", BigDecimal.TEN, 1);
        cart.addItem(item);
        LocalDateTime initialUpdate = cart.getUpdatedAt();

        cart.updateItemQuantity(productId, 5);

        assertEquals(5, cart.getItems().get(0).quantity());
    }

    @Test
    void updateItemQuantity_NonExistingItem_ShouldNotModifyCart() {
        Cart cart = new Cart(userId);
        LocalDateTime initialUpdate = cart.getUpdatedAt();

        cart.updateItemQuantity(UUID.randomUUID(), 5);

        assertTrue(cart.getItems().isEmpty());
        assertEquals(initialUpdate, cart.getUpdatedAt());
    }

    @Test
    void clear_NonEmptyCart_ShouldRemoveAllItems() {
        Cart cart = new Cart(userId);
        cart.addItem(new CartItem(UUID.randomUUID(), "A", BigDecimal.ONE, 1));
        cart.addItem(new CartItem(UUID.randomUUID(), "B", BigDecimal.TEN, 2));
        LocalDateTime initialUpdate = cart.getUpdatedAt();

        cart.clear();

        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void clear_EmptyCart_ShouldNotModify() {
        Cart cart = new Cart(userId);
        LocalDateTime initialUpdate = cart.getUpdatedAt();

        cart.clear();

        assertTrue(cart.getItems().isEmpty());
        assertEquals(initialUpdate, cart.getUpdatedAt());
    }

    @Test
    void getItems_ShouldReturnUnmodifiableList() {
        Cart cart = new Cart(userId);
        cart.addItem(new CartItem(productId, "Product", BigDecimal.TEN, 1));


        assertThrows(UnsupportedOperationException.class, () -> {
            cart.getItems().add(new CartItem(UUID.randomUUID(), "New", BigDecimal.ONE, 1));
        });
    }
}

