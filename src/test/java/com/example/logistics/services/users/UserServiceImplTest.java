package bg.nbu.logistics.services.users;

import static bg.nbu.logistics.commons.constants.RoleConstants.ROLE_COURIER;
import static bg.nbu.logistics.commons.constants.RoleConstants.ROLE_EMPLOYEE;
import static bg.nbu.logistics.commons.constants.RoleConstants.ROLE_ROOT;
import static bg.nbu.logistics.commons.constants.RoleConstants.ROLE_USER;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
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

import bg.nbu.logistics.domain.entities.Role;
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
    private Role roleMock;

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
        lenient().when(modelMapperMock.map(roleServiceModelMock, Role.class))
                .thenReturn(roleMock);
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
        lenient().when(userRepositoryMock.findAll())
                .thenReturn(singletonList(userMock));
        lenient().when(userRepositoryMock.findById(ID))
                .thenReturn(of(userMock));
        lenient().when(roleServiceMock.findByAuthority(anyString()))
                .thenReturn(roleServiceModelMock);
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
    void testFindAllUsers() {
        when(userMock.getAuthorities()).thenReturn(singleton(new Role(ROLE_USER)));

        assertThat(userService.findAllUsers(), contains(userServiceModelMock));
    }

    @Test
    void testFindAllUsersReturnsEmptyCollectionWhenNotUsersFound() {
        when(userRepositoryMock.findAll()).thenReturn(emptyList());

        assertThat(userService.findAllUsers(), Matchers.<UserServiceModel>empty());
    }

    @Test
    void testFindAllUsersReturnsEmptyCollectionWhenNotUserRole() {
        when(userMock.getAuthorities()).thenReturn(singleton(new Role(ROLE_ROOT)));

        assertThat(userService.findAllUsers(), Matchers.<UserServiceModel>empty());
    }

    @Test
    void testFindAllEmployeesWhenEmployeeRole() {
        when(userMock.getAuthorities()).thenReturn(singleton(new Role(ROLE_EMPLOYEE)));

        assertThat(userService.findAllEmployees(), contains(userServiceModelMock));
    }

    @Test
    void testFindAllEmployeesWhenCourierRole() {
        when(userMock.getAuthorities()).thenReturn(singleton(new Role(ROLE_COURIER)));

        assertThat(userService.findAllEmployees(), contains(userServiceModelMock));
    }

    @Test
    void testFindAllEmployeesReturnsEmptyCollectionWhenNotUsersFound() {
        when(userRepositoryMock.findAll()).thenReturn(emptyList());

        assertThat(userService.findAllEmployees(), Matchers.<UserServiceModel>empty());
    }

    @Test
    void testFindAllEmployeesWhenNotEmployeeRole() {
        when(userMock.getAuthorities()).thenReturn(singleton(new Role(ROLE_ROOT)));

        assertThat(userService.findAllEmployees(), Matchers.<UserServiceModel>empty());
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

    @Test
    void testChangeRoleByIdThrowsExceptionWhenUserNotFound() {
        when(userRepositoryMock.findById(ID)).thenReturn(empty());

        assertThrows(NoSuchElementException.class, () -> userService.changeRoleById(ID, ROLE_ROOT));
    }

    @Test
    void testChangeRoleById() {
        userService.changeRoleById(ID, ROLE_ROOT);

        verify(userMock).setAuthorities(singleton(roleMock));
        verify(userRepositoryMock).saveAndFlush(userMock);
    }
}
