package com.ecommerce.user_service.security.userprinciple;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ecommerce.user_service.model.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.Accessors;

@Data
@With
@Builder
@Accessors(fluent = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserPrinciple implements UserDetails {
    private Long id;
    private String fullname;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    private String gender;
    private String phone;
    private String avatar;
    private Collection<? extends GrantedAuthority> roles;

    public static UserPrinciple build(User user) {
        List<GrantedAuthority> authorityList = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return UserPrinciple.builder()
                .id(user.getId())
                .fullname(user.getFullname())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .gender(user.getGender())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .roles(authorityList)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
