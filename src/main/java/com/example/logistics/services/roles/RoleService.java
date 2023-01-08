package com.example.logistics.services.roles;

import java.util.Set;

import com.example.logistics.domain.models.service.RoleServiceModel;

public interface RoleService {
    Set<RoleServiceModel> findAllRoles();

    RoleServiceModel findByAuthority(String authority);
}