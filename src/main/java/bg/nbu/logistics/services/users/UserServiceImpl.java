package bg.nbu.logistics.services.users;

import static bg.nbu.logistics.commons.constants.AuthorizationConstants.UNABLE_TO_FIND_USER_BY_NAME_MESSAGE;
import static bg.nbu.logistics.commons.constants.RoleConstants.*;
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

import bg.nbu.logistics.domain.entities.Role;
import bg.nbu.logistics.domain.entities.User;
import bg.nbu.logistics.domain.models.service.RoleServiceModel;
import bg.nbu.logistics.domain.models.service.UserServiceModel;
import bg.nbu.logistics.repositories.UserRepository;
import bg.nbu.logistics.services.roles.RoleService;

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
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<UserServiceModel> findByUsername(String username) {
        final Optional<User> user = userRepository.findByUsername(username);

        return user.isPresent() ? of(modelMapper.map(user.get(), UserServiceModel.class)) : empty();
    }
}
