package ru.ecommerce.cartservice.infrastructure.config;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import ru.ecommerce.cartservice.infrastructure.repository.DataMongoRepository;

@EnableMongoRepositories(basePackageClasses = DataMongoRepository.class)
public class MongoDBConfig {
}
