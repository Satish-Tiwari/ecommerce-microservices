package com.ecommerce.user_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.user_service.model.entity.Role;
import com.ecommerce.user_service.model.entity.RoleName;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(@Param("name") RoleName name);
    boolean existsByName(RoleName name);

    @Query("SELECT u.roles FROM User u WHERE u.id = :id")
    List<Role> findByUserId(@Param("id") Long id);
}
