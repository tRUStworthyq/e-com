package ru.ecommerce.orderservice.infrastructure.integration.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.ecommerce.orderservice.application.dto.request.CartItem;
import ru.ecommerce.orderservice.application.dto.response.OrderDto;
import ru.ecommerce.orderservice.application.mapper.OrderMapper;
import ru.ecommerce.orderservice.application.service.AppOrderService;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order")
@AllArgsConstructor
@Tag(name = "Заказы", description = "Управление заказами")
public class OrderController {

    @Autowired
    private AppOrderService orderService;

    @Operation(
            summary = "Получить заказы пользователя",
            description = "Возвращает список заказов с пагинацией",
            parameters = {
                    @Parameter(
                            name = "page",
                            in = ParameterIn.QUERY,
                            description = "Номер страницы (начиная с 0)",
                            example = "0",
                            required = true
                    ),
                    @Parameter(
                            name = "size",
                            in = ParameterIn.QUERY,
                            description = "Количество элементов на странице",
                            example = "10",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Заказы успешно получены",
                            content = @Content(schema = @Schema(implementation = OrderDto[].class))
                    )
            }
    )
    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> findOrdersByUserId(@Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt, @RequestParam("page") int page, @RequestParam("size") int size) {
        UUID userId = UUID.fromString(jwt.getSubject());
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(orderService.findOrdersByUserId(userId, pageable).stream()
                .map(OrderMapper::toDto)
                .toList()
        );
    }

    @Operation(
            summary = "Получить заказ по ID",
            description = "Возвращает детали заказа по его UUID",
            parameters = {
                    @Parameter(
                            name = "id",
                            in = ParameterIn.PATH,
                            description = "UUID заказа",
                            example = "123e4567-e89b-12d3-a456-426614174000",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Заказ найден",
                            content = @Content(schema = @Schema(implementation = OrderDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Заказ не найден")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> findOrderById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(OrderMapper.toDto(orderService.findById(id)));
    }

    @Operation(
            summary = "Создать заказ",
            description = "Создает новый заказ из элементов корзины",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Заказ успешно создан",
                            content = @Content(schema = @Schema(implementation = OrderDto.class))
                    )
            }
    )
    @PostMapping("/create")
    public ResponseEntity<OrderDto> createOrder(@Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt, @RequestBody List<CartItem> items) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(OrderMapper.toDto(orderService.createOrder(userId, items)));
    }

    @Operation(
            summary = "Отменить заказ",
            description = "Отменяет заказ по его UUID",
            parameters = {
                    @Parameter(
                            name = "id",
                            in = ParameterIn.PATH,
                            description = "UUID заказа",
                            example = "123e4567-e89b-12d3-a456-426614174000",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Заказ успешно отменен"),
                    @ApiResponse(responseCode = "400", description = "Нет прав для отмены заказа"),
                    @ApiResponse(responseCode = "404", description = "Заказ не найден")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(@Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt, @PathVariable("id") UUID id) {
        UUID userId = UUID.fromString(jwt.getSubject());
        try {
            orderService.cancelOrder(id, userId);
            return ResponseEntity.ok().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
