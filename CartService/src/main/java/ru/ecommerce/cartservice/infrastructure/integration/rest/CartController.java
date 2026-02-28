package ru.ecommerce.cartservice.infrastructure.integration.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Tag(name = "Корзина", description = "Управление корзиной покупок")
public class CartController {

    @Autowired
    private AppCartService cartService;

    @Operation(
            summary = "Получить или создать корзину",
            description = "Возвращает текущую корзину пользователя или создает новую",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Корзина успешно получена",
                            content = @Content(schema = @Schema(implementation = CartDto.class))
                    )
            }
    )
    @GetMapping("/")
    public ResponseEntity<CartDto> getOrCreateCart(@Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(CartMapper.toDto(cartService.getOrCreateCart(userId)));
    }

    @Operation(
            summary = "Добавить товар в корзину",
            description = "Добавляет новый товар в корзину",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Товар успешно добавлен",
                            content = @Content(schema = @Schema(implementation = CartDto.class))
                    )
            }
    )
    @PostMapping("/add")
    public ResponseEntity<CartDto> addItemToCart(@Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt, @RequestBody @Valid CartItemDto itemDto) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(CartMapper.toDto(cartService.addItemToCart(userId, CartMapper.toCartItem(itemDto))));
    }

    @Operation(
            summary = "Изменить количество товара",
            description = "Обновляет количество указанного товара в корзине",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Количество успешно изменено",
                            content = @Content(schema = @Schema(implementation = CartDto.class))
                    )
            }
    )
    @PatchMapping("/")
    public ResponseEntity<CartDto> updateItemQuantity(@Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt, @RequestBody @Valid ItemQuantityDto itemQuantityDto) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(CartMapper.toDto(cartService.updateItemQuantity(userId, itemQuantityDto.productId(), itemQuantityDto.quantity())));
    }


    @Operation(
            summary = "Удалить товар из корзины",
            description = "Полностью удаляет товар из корзины",
            parameters = {
                    @Parameter(
                            name = "id",
                            in = ParameterIn.PATH,
                            description = "UUID товара",
                            required = true,
                            example = "123e4567-e89b-12d3-a456-426614174000"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Товар успешно удален",
                            content = @Content(schema = @Schema(implementation = CartDto.class))
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<CartDto> removeItemFromCart(@Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt, @PathVariable("id") UUID productId) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(CartMapper.toDto(cartService.removeItemFromCart(userId, productId)));
    }

    @Operation(
            summary = "Очистить корзину",
            description = "Удаляет все товары из корзины",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Корзина успешно очищена")
            }
    )
    @DeleteMapping("/")
    public ResponseEntity<Void> clearCart(@Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }
}
