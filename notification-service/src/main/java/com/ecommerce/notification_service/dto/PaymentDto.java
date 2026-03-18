package com.ecommerce.notification_service.dto;

import com.ecommerce.notification_service.entity.PaymentStatus;

import lombok.*;
import java.io.Serializable;
import java.io.Serial;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PaymentDto {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer paymentId;
    private Boolean isPayed;
    private PaymentStatus paymentStatus;

    private Integer orderId;
    private Long userId;
}
