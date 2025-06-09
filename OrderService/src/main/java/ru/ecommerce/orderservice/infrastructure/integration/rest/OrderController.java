package ru.ecommerce.orderservice.infrastructure.integration.rest;

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
public class OrderController {

    @Autowired
    private AppOrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> findOrdersByUserId(@AuthenticationPrincipal Jwt jwt, @RequestParam("page") int page, @RequestParam("size") int size) {
        UUID userId = UUID.fromString(jwt.getSubject());
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(orderService.findOrdersByUserId(userId, pageable).stream()
                .map(OrderMapper::toDto)
                .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> findOrderById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(OrderMapper.toDto(orderService.findById(id)));
    }

    @PostMapping("/create")
    public ResponseEntity<OrderDto> createOrder(@AuthenticationPrincipal Jwt jwt, @RequestBody List<CartItem> items) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(OrderMapper.toDto(orderService.createOrder(userId, items)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(@AuthenticationPrincipal Jwt jwt, @PathVariable("id") UUID id) {
        UUID userId = UUID.fromString(jwt.getSubject());
        try {
            orderService.cancelOrder(id, userId);
            return ResponseEntity.ok().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
