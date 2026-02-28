package com.ecommerce.user_service.model.entity;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.ManyToAny;
import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userId", unique = true, nullable = false, updatable = false)
    private Long id;

    @NotBlank(message = "fullName is required")
    @Size(min=3, max=100, message = "fullName must be between 3 and 100 characters")
    @Column(name = "fullName", nullable = false)
    private String fullname;


    @NotBlank(message = "username is required")
    @Size(min=3, max=100, message = "username must be between 3 and 100 characters")
    @Column(name = "userName", nullable = false)
    private String username;

    @NaturalId
    @NotBlank(message = "email is required")
    @Size(max=50, message = "email must be less than 50 characters")
    @Email(message = "Input must be a in email format")
    @Column(name = "email")
    private String email;

    @JsonIgnore
    @NotNull(message="Password must not be null")
    @Size(min=6, max=100, message = "password must be between 6 and 100 characters")
    @Column(name = "password")
    private String password;

    @NotBlank(message="Gender must not be blank")
    @Column(name = "gender", nullable = false)
    private String gender;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "phone number must be between 10 and 15 digits and can start with +")
    @Size(min=10, max=15, message = "phone number must be between 10 and 15 characters")
    @Column(name = "phone", nullable = false)
    private String phone;

    @Pattern(regexp = "^(http|https)://.*$", message = "Avatar URL must be a valid HTTP or HTTPS URL")
    @Lob
    @Column(name="imageUrl")
    private String avatar;

    @ManyToAny(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinTable(name = "user_role",
        joinColumns = @jakarta.persistence.JoinColumn(name = "user_id"),
        inverseJoinColumns = @jakarta.persistence.JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}
