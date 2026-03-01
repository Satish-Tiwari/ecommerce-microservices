package com.ecommerce.user_service.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.user_service.model.entity.User;

@Repository
public interface UserRepositoryPaging extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u")
    List<User> findAll(Sort sort);
}
