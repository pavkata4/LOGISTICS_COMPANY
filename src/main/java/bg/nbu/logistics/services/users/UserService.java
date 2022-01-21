package bg.nbu.logistics.services.users;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import bg.nbu.logistics.domain.models.service.UserServiceModel;

public interface UserService extends UserDetailsService {
    UserServiceModel register(UserServiceModel userServiceModel);

    List<UserServiceModel> findAll();

    UserServiceModel findById(long id);

    void delete(long id);

    Optional<UserServiceModel> findByUsername(String username);
}
