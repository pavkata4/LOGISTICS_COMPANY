package bg.nbu.logistics.services.users;

import static bg.nbu.logistics.commons.constants.RoleConstants.ROLE_USER;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import bg.nbu.logistics.domain.entities.User;
import bg.nbu.logistics.domain.models.service.RoleServiceModel;
import bg.nbu.logistics.domain.models.service.UserServiceModel;
import bg.nbu.logistics.repositories.UserRepository;
import bg.nbu.logistics.services.roles.RoleService;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";
    private static final long ID = 1;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private RoleService roleServiceMock;

    @Mock
    private ModelMapper modelMapperMock;

    @Mock
    private PasswordEncoder passwordEncoderMock;

    @Mock
    private User userMock;

    @Mock
    private UserServiceModel userServiceModelMock;

    @Mock
    private RoleServiceModel roleServiceModelMock;

    @BeforeEach
    public void setUp() {
        lenient().when(userRepositoryMock.findByUsername(USERNAME))
                .thenReturn(of(userMock));
        lenient().when(modelMapperMock.map(userMock, UserServiceModel.class))
                .thenReturn(userServiceModelMock);
        lenient().when(modelMapperMock.map(userServiceModelMock, User.class))
                .thenReturn(userMock);
        lenient().when(roleServiceMock.findAllRoles())
                .thenReturn(Set.of(roleServiceModelMock));
        lenient().when(userRepositoryMock.count())
                .thenReturn(1L);
        lenient().when(userServiceModelMock.getPassword())
                .thenReturn(PASSWORD);
        lenient().when(passwordEncoderMock.encode(PASSWORD))
                .thenReturn(ENCODED_PASSWORD);
        lenient().when(userRepositoryMock.saveAndFlush(userMock))
                .thenReturn(userMock);
    }

    @Test
    void testLoadUserByUsernameUserNotFound() {
        when(userRepositoryMock.findByUsername(USERNAME)).thenReturn(empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(USERNAME));
    }

    @Test
    void testLoadUserByUsername() {
        assertThat(userService.loadUserByUsername(USERNAME), equalTo(userMock));
    }

    @Test
    void testRegisterInitialUserRolesSetup() {
        when(userRepositoryMock.count()).thenReturn(0L);

        assertThat(userService.register(userServiceModelMock), equalTo(userServiceModelMock));

        verify(userServiceModelMock).setAuthorities(Set.of(roleServiceModelMock));
        verify(userMock).setPassword(ENCODED_PASSWORD);
        verify(userRepositoryMock).saveAndFlush(userMock);
    }

    @Test
    void testRegisterDefaultRolesSetup() {
        assertThat(userService.register(userServiceModelMock), equalTo(userServiceModelMock));

        verify(roleServiceMock).findByAuthority(ROLE_USER);
        verify(userMock).setPassword(ENCODED_PASSWORD);
        verify(userRepositoryMock).saveAndFlush(userMock);
    }

    @Test
    void testFindAllNoUsersPresent() {
        when(userRepositoryMock.findAll()).thenReturn(emptyList());

        assertThat(userService.findAll(), Matchers.empty());
    }

    @Test
    void testFindAll() {
        when(userRepositoryMock.findAll()).thenReturn(singletonList(userMock));

        assertThat(userService.findAll(), contains(userServiceModelMock));
    }

    @Test
    void testDelete() {
        userService.delete(ID);

        verify(userRepositoryMock).deleteById(ID);
    }

    @Test
    void testFindByUsernameUserNotFound() {
        when(userRepositoryMock.findByUsername(USERNAME)).thenReturn(empty());

        assertThat(userService.findByUsername(USERNAME), equalTo(Optional.<UserServiceModel>empty()));
    }

    @Test
    void testFindByUsername() {
        assertThat(userService.findByUsername(USERNAME), equalTo(of(userServiceModelMock)));
    }
}
