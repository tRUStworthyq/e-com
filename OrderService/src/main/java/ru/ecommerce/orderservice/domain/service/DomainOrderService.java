package ru.ecommerce.orderservice.domain.service;

import org.springframework.data.domain.Pageable;
import ru.ecommerce.orderservice.application.dto.request.CartItem;
import ru.ecommerce.orderservice.domain.exception.EntityNotFoundException;
import ru.ecommerce.orderservice.domain.model.Order;
import ru.ecommerce.orderservice.domain.repository.OrderRepository;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

public class DomainOrderService implements OrderService {
    private OrderRepository orderRepository;

    public DomainOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(UUID userId, List<CartItem> cartItems) {
        Order order = Order.create(userId, cartItems);
        return orderRepository.save(order);
    }

    @Override
    public boolean cancelOrder(UUID id, UUID userId) throws AccessDeniedException {
        Order order = orderRepository.findByIdAndStatus(id)
                .orElseThrow(() -> new EntityNotFoundException("Order with id " + id + " not exists"));
        if (!userId.equals(order.getUserId())) {
            throw new AccessDeniedException("Access denied");
        }
        boolean isCancel = order.cancel();
        if (isCancel) {
            orderRepository.save(order);
        }
        return isCancel;
    }


    @Override
    public List<Order> findOrdersByUserId(UUID userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable).toList();
    }

    @Override
    public boolean acceptOrder(UUID orderId) {
        Order order = findPendingOrderById(orderId);
        boolean isAccepted = order.acceptOrder();
        orderRepository.save(order);
        return isAccepted;
    }

    @Override
    public Order findPendingOrderById(UUID id) {
        return orderRepository.findByIdPendingOrders(id)
                .orElseThrow(() -> new EntityNotFoundException("Order with id " + id + " not exists"));
    }

    @Override
    public Order findById(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order with id " + id + " not exists"));
    }
}
