package com.ecommerce.notification_service.config.kafka;

import org.springframework.boot.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class ReactiveKafkaProperties {
    @Value("${kafka.bootstrap-servers}")
    String bootstrapServers;

    @Value("${payment.kafka.consumer-group-id}")
    Sting consumerGroupId;
}
