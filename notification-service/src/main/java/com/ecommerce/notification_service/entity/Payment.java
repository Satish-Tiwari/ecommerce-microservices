package com.ecommerce.notification_service.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "payments")
public class Payment {
    @Id
    private String id;

    private Integer paymentId;
    private Boolean isPayed;
    private PaymentStatus paymentStatus;

    private Integer orderId;
    private Long userId;
}
