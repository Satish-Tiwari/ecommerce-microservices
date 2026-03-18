package com.ecommerce.notification_service.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "notifications")
public class Notification {
    @Id
    private String id;
   
    private String content;
    private String recipientId;
    private boolean read;

    @Field(name="timestamp")
    private LocalDateTime timestamp;
    private String notificationType;
    private String link;

    public Notification() {
        this.timestamp = LocalDateTime.now();
    }
}
