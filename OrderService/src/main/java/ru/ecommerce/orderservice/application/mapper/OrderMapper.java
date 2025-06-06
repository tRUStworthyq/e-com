package ru.ecommerce.orderservice.application.mapper;

import ru.ecommerce.orderservice.application.dto.response.OrderDto;
import ru.ecommerce.orderservice.domain.model.Order;

public class OrderMapper {

    public static OrderDto toDto(Order order) {
        return new OrderDto(
                order.getId(),
                order.getOrderItems(),
                order.getPrice(),
                order.getCreatedAt()
        );
    }
}
