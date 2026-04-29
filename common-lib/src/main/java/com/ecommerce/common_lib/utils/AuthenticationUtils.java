package com.ecommerce.common_lib.utils;

import com.ecommerce.common_lib.constants.ApiConstant;
import com.ecommerce.common_lib.exception.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public final class AuthenticationUtils {

    private AuthenticationUtils() {
    }

    public static String extractUserId() {
        Authentication authentication = getAuthentication();

        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new AccessDeniedException(ApiConstant.ACCESS_DENIED);
        }

        if (!(authentication instanceof JwtAuthenticationToken contextHolder)) {
            throw new AccessDeniedException(ApiConstant.ACCESS_DENIED);
        }

        return contextHolder.getToken().getSubject();
    }

    public static String extractJwt() {
        return ((Jwt) getAuthentication().getPrincipal()).getTokenValue();
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}