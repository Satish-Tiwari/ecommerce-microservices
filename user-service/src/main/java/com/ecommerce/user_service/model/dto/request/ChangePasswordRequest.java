package com.ecommerce.user_service.model.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ChangePasswordRequest {
    String oldPassword;
    String newPassword;
    String confirmPassword;
}
