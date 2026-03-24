package com.ecommerce.notification_service.entity;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    
    NOT_STARTED("Not Started"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed");

    private final String status;

    PaymentStatus(String status) {
        this.status = status;
    }
}
