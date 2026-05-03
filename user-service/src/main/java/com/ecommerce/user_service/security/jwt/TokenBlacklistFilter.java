package com.ecommerce.user_service.security.jwt;

import com.ecommerce.user_service.repository.UserRepository;
import com.ecommerce.user_service.model.entity.User;
import com.ecommerce.user_service.model.dto.response.ResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenBlacklistFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Allow logout requests to pass through so they can return the specific
        // "already logged out" message
        if (request.getRequestURI().endsWith("/api/auth/logout")) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String token = null;
            Object principal = authentication.getPrincipal();

            if (principal instanceof Jwt jwt) {
                token = jwt.getTokenValue();
            } else if (authentication.getCredentials() instanceof String credentials) {
                token = credentials;
            }

            if (token != null) {
                String username = authentication.getName();
                User user = userRepository.findByUsername(username).orElse(null);

                if (user == null || user.getAccessToken() == null || !user.getAccessToken().equals(token)) {
                    SecurityContextHolder.clearContext();
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    ResponseMessage errorResponse = new ResponseMessage(
                            "Token has been revoked or is invalid. Please log in again.");
                    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
