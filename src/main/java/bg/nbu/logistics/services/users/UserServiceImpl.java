package bg.nbu.logistics.services.users;

import static bg.nbu.logistics.commons.constants.AuthorizationConstants.UNABLE_TO_FIND_USER_BY_NAME_MESSAGE;
import static bg.nbu.logistics.commons.constants.RoleConstants.ROLE_USER;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import bg.nbu.logistics.domain.entities.User;
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
    public List<UserServiceModel> findAll() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserServiceModel.class))
                .collect(toUnmodifiableList());
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
