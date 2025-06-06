package ru.ecommerce.cartservice.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.ecommerce.cartservice.domain.repository.CartRepository;
import ru.ecommerce.cartservice.domain.service.CartService;
import ru.ecommerce.cartservice.domain.service.CartServiceImpl;

@Configuration
public class DomainConfig {

    @Bean
    public CartService cartService(CartRepository cartRepository) {
        return new CartServiceImpl(cartRepository);
    }
}
