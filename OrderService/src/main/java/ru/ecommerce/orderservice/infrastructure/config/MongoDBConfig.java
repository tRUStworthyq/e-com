package ru.ecommerce.orderservice.infrastructure.config;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import ru.ecommerce.orderservice.infrastructure.repository.DataMongoRepository;

@EnableMongoRepositories(basePackageClasses = DataMongoRepository.class)
public class MongoDBConfig {
}
