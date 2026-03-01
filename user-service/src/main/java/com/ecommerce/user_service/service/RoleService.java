package com.ecommerce.user_service.service;

import java.util.List;
import java.util.Optional;

import com.ecommerce.user_service.model.entity.Role;
import com.ecommerce.user_service.model.entity.RoleName;

public interface RoleService {
    Optional<Role> findByName(RoleName roleName);

    boolean assignRole(Long id, String roleName);

    boolean revokeRole(Long id, String roleName);

    List<String> getUserRoles(Long id);
}
