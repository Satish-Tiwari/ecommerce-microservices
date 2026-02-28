package com.ecommerce.user_service.security.userprinciple;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.ecommerce.user_service.exception.wrapper.EmailOrUsernameNotFoundException;
import com.ecommerce.user_service.model.entity.User;
import com.ecommerce.user_service.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EmailOrUsernameNotFoundException(
                        "Email or user does not exists. Try again letter..." + username));
        return UserPrinciple.build(user);
    }

    @Transactional
    public UserDetails loadUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EmailOrUsernameNotFoundException(
                "Email or user does not exists. Try again letter..." + email));
        return UserPrinciple.build(user);
    }

    @Transactional
    public UserDetails loadUserByPhone(String phone) {
        User user = userRepository.findByPhone(phone).orElseThrow(() -> new EmailOrUsernameNotFoundException(
                "Email or user does not exists. Try again letter..." + phone));
        return UserPrinciple.build(user);
    }
}
