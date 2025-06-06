package ru.ecommerce.cartservice.infrastructure.integration.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.ecommerce.cartservice.application.dto.CartDto;
import ru.ecommerce.cartservice.application.dto.CartItemDto;
import ru.ecommerce.cartservice.application.dto.ItemQuantityDto;
import ru.ecommerce.cartservice.application.mapper.CartMapper;
import ru.ecommerce.cartservice.application.service.AppCartService;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
public class CartController {

    private AppCartService cartService;

    @GetMapping("/")
    public ResponseEntity<CartDto> getOrCreateCart(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(CartMapper.toDto(cartService.getOrCreateCart(userId)));
    }

    @PostMapping("/add")
    public ResponseEntity<CartDto> addItemToCart(@AuthenticationPrincipal Jwt jwt, CartItemDto itemDto) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(CartMapper.toDto(cartService.addItemToCart(userId, CartMapper.toCartItem(itemDto))));
    }

    @PatchMapping("/")
    public ResponseEntity<CartDto> updateItemQuantity(@AuthenticationPrincipal Jwt jwt, @RequestBody ItemQuantityDto itemQuantityDto) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(CartMapper.toDto(cartService.updateItemQuantity(userId, itemQuantityDto.productId(), itemQuantityDto.quantity())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CartDto> removeItemFromCart(@AuthenticationPrincipal Jwt jwt, @PathVariable("id") UUID productId) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(CartMapper.toDto(cartService.removeItemFromCart(userId, productId)));
    }

    @DeleteMapping("/")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }

}
