package ru.ecommerce.orderservice.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.ecommerce.orderservice.OrderServiceApplication;
import ru.ecommerce.orderservice.domain.repository.OrderRepository;
import ru.ecommerce.orderservice.domain.service.DomainOrderService;
import ru.ecommerce.orderservice.domain.service.OrderService;

@Configuration
@ComponentScan(basePackageClasses = OrderServiceApplication.class)
public class DomainConfig {

    @Bean
    OrderService orderService(OrderRepository orderRepository) {
        return new DomainOrderService(orderRepository);
    }
}
