package com.ecommerce.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.user_service.model.entity.User;

@Repository
public interface UserRepositoryPaging extends JpaRepository<User, Long> {
}
