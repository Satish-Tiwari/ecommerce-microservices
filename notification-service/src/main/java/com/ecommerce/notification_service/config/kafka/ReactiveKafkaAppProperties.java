package com.ecommerce.notification_service.config.kafka;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Data
@Configuration
public class ReactiveKafkaAppProperties {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${payment.kafka.consumer-group-id}")
    private String consumerGroupId;
}
