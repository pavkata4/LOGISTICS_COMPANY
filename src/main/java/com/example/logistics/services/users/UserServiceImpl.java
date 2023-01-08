package com.example.logistics.services.users;



import static com.example.logistics.commons.constants.AuthorizationConstants.UNABLE_TO_FIND_USER_BY_NAME_MESSAGE;
import static com.example.logistics.commons.constants.RoleConstants.ROLE_EMPLOYEE;
import static com.example.logistics.commons.constants.RoleConstants.ROLE_USER;
import static com.example.logistics.commons.constants.RoleConstants.ROLE_COURIER;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.logistics.domain.entities.Role;
import com.example.logistics.domain.entities.User;
import com.example.logistics.domain.models.service.RoleServiceModel;
import com.example.logistics.domain.models.service.UserServiceModel;
import com.example.logistics.repositories.UserRepository;
import com.example.logistics.services.roles.RoleService;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, ModelMapper modelMapper,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(UNABLE_TO_FIND_USER_BY_NAME_MESSAGE));
    }

    @Override
    public UserServiceModel register(UserServiceModel userServiceModel) {
        if (userRepository.count() == 0) {
            userServiceModel.setAuthorities(roleService.findAllRoles());
        } else {
            userServiceModel.getAuthorities()
                    .add(roleService.findByAuthority(ROLE_USER));
        }

        final User user = modelMapper.map(userServiceModel, User.class);
        user.setPassword(passwordEncoder.encode(userServiceModel.getPassword()));

        return modelMapper.map(userRepository.saveAndFlush(user), UserServiceModel.class);
    }

    @Override
    public UserServiceModel update(UserServiceModel userServiceModel) {
//        final User user = modelMapper.map(userServiceModel, User.class);
//        user.setPassword(passwordEncoder.encode(userServiceModel.getPassword()));
//        user.setAuthorities(modelMapper.map(userServiceModel.getAuthorities(), new HashSet<Role>().getClass()));
        User user = userRepository.findById(userServiceModel.getId()).orElseThrow();
        user.setUsername(userServiceModel.getUsername());

        return userRepository.findById(userServiceModel.getId())
                .map(updateUser -> modelMapper.map(userRepository.saveAndFlush(user), UserServiceModel.class)).orElseThrow();
    }

    @Override
    public List<UserServiceModel> findAllUsers() {
        List<User> list = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            for (Role role : user.getAuthorities()) {
                if (role.getAuthority()
                        .equals(ROLE_USER)) {
                    list.add(user);
                    break;
                }
            }
        }
        return list.stream()
                .map(user -> modelMapper.map(user, UserServiceModel.class))
                .collect(toUnmodifiableList());
    }

    @Override
    public List<UserServiceModel> findAllEmployees() {
        List<User> list = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            for (Role role : user.getAuthorities()) {
                if (role.getAuthority()
                        .equals(ROLE_EMPLOYEE)
                        || role.getAuthority()
                                .equals(ROLE_COURIER)) {
                    list.add(user);
                    break;
                }
            }
        }
        return list.stream()
                .map(user -> modelMapper.map(user, UserServiceModel.class))
                .collect(toUnmodifiableList());
    }

    @Override
    public UserServiceModel findById(long id) {
        User user = userRepository.findById(id).get();
        UserServiceModel userServiceModel = new UserServiceModel();
        userServiceModel.setId(user.getId());
        userServiceModel.setUsername(user.getUsername());
        userServiceModel.setPassword(user.getPassword());
        Set<RoleServiceModel> roleServiceModelList = new HashSet<>();
        RoleServiceModel roleServiceModel = new RoleServiceModel();
        for (Role role : user.getAuthorities()) {
            roleServiceModel.setAuthority(role.getAuthority());
            roleServiceModelList.add(roleServiceModel);
        }
        userServiceModel.setAuthorities(roleServiceModelList);

        return userServiceModel;
    }

    @Override
    public Optional<UserServiceModel> findByUsername(String username) {
        final Optional<User> user = userRepository.findByUsername(username);

        return user.isPresent() ? of(modelMapper.map(user.get(), UserServiceModel.class)) : empty();
    }

    @Override
    public void changeRoleById(long id, String authority) {
        User user = userRepository.findById(id).orElseThrow();
        Set<Role> roles = new HashSet<>();
        roles.add(modelMapper.map(roleService.findByAuthority(authority), Role.class));
        user.setAuthorities(roles);
        userRepository.saveAndFlush(user);
    }

    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }

}
