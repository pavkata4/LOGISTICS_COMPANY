package com.example.logistics.services.users;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.logistics.domain.models.service.UserServiceModel;

public interface UserService extends UserDetailsService {
    UserServiceModel register(UserServiceModel userServiceModel);

    UserServiceModel update(UserServiceModel userServiceModel);

    List<UserServiceModel> findAllUsers();

    List<UserServiceModel> findAllEmployees();

    UserServiceModel findById(long id);

    Optional<UserServiceModel> findByUsername(String username);

    void changeRoleById(long id, String authority);

    void delete(long id);
}
