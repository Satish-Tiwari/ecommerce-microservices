package com.ecommerce.user_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEvent {
    private String email;
    private String firstName;
    private String lastName;
    private String type; // e.g., "USER_CREATED", "USER_LOGOUT"
}
