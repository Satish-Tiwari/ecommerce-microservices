package com.ecommerce.notification_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEvent {
    private String email;
    private String firstName;
    private String lastName;
    private String type; // USER_CREATED, USER_LOGOUT
}
