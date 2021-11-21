package bg.nbu.logistics.services.roles;

import java.util.Set;

import bg.nbu.logistics.domain.models.service.RoleServiceModel;

public interface RoleService {
    Set<RoleServiceModel> findAllRoles();

    RoleServiceModel findByAuthority(String authority);
}