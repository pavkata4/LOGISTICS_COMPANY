package bg.nbu.logistics.services.offices;

import static bg.nbu.logistics.commons.constants.RoleConstants.ROLE_COURIER;
import static bg.nbu.logistics.commons.constants.RoleConstants.ROLE_EMPLOYEE;
import static bg.nbu.logistics.commons.constants.RoleConstants.ROLE_ROOT;
import static bg.nbu.logistics.commons.constants.RoleConstants.ROLE_USER;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.util.collections.Sets.newSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import bg.nbu.logistics.commons.utils.Mapper;
import bg.nbu.logistics.domain.entities.Office;
import bg.nbu.logistics.domain.entities.User;
import bg.nbu.logistics.domain.models.service.OfficeServiceModel;
import bg.nbu.logistics.domain.models.service.RoleServiceModel;
import bg.nbu.logistics.domain.models.service.UserServiceModel;
import bg.nbu.logistics.repositories.OfficeRepository;
import bg.nbu.logistics.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith(MockitoExtension.class)
class OfficeServiceImplTest {
    private static final String ADDRESS = "address";
    private static final long ID = 0;

    @InjectMocks
    private OfficeServiceImpl officeService;

    @Mock
    private ModelMapper modelMapperMock;

    @Mock
    private OfficeRepository officeRepositoryMock;

    @Mock
    private UserService userServiceMock;

    @Mock
    private Mapper mapperMock;

    @Mock
    private Office officeMock;

    @Mock
    private OfficeServiceModel officeServiceModelMock;

    @Mock
    private UserServiceModel userServiceModelMock;

    @Mock
    private RoleServiceModel roleServiceModelMock;

    @Mock
    private User userMock;

    private final ModelMapper modelMapper;

    OfficeServiceImplTest(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @BeforeEach
    void setUp() {
        lenient().when(mapperMock.mapCollection(singletonList(officeMock), OfficeServiceModel.class))
                .thenReturn(singletonList(officeServiceModelMock));
        lenient().when(modelMapperMock.map(officeMock, OfficeServiceModel.class))
                .thenReturn(officeServiceModelMock);
        lenient().when(modelMapperMock.map(userServiceModelMock, User.class))
                .thenReturn(userMock);

        lenient().when(officeRepositoryMock.findById(ID))
                .thenReturn(of(officeMock));
        lenient().when(officeRepositoryMock.findByAddress(ADDRESS))
                .thenReturn(of(officeMock));

        lenient().when(userServiceModelMock.getAuthorities())
                .thenReturn(singleton(roleServiceModelMock));

        lenient().when(roleServiceModelMock.getAuthority())
                .thenReturn(ROLE_USER);

        lenient().when(officeMock.getEmployees())
                .thenReturn(newSet(userMock));
    }

    @Test
    void addEmployeeWhenUserRole() {
        officeService.addEmployee(ID, userServiceModelMock);

        verify(officeMock).setEmployees(singleton(userMock));
        verify(officeRepositoryMock).saveAndFlush(officeMock);
    }

    @Test
    void addEmployeeWhenRootRole() {
        when(roleServiceModelMock.getAuthority()).thenReturn(ROLE_ROOT);

        officeService.addEmployee(ID, userServiceModelMock);

        verify(officeMock).setEmployees(singleton(userMock));
        verify(officeRepositoryMock).saveAndFlush(officeMock);
    }

    @Test
    void addEmployeeWhenCourierRole() {
        when(roleServiceModelMock.getAuthority()).thenReturn(ROLE_COURIER);

        officeService.addEmployee(ID, userServiceModelMock);

        verifyNoInteractions(userServiceMock, officeRepositoryMock);
    }

    @Test
    void addEmployeeWhenEmployeeRole() {
        when(roleServiceModelMock.getAuthority()).thenReturn(ROLE_EMPLOYEE);

        officeService.addEmployee(ID, userServiceModelMock);

        verifyNoInteractions(userServiceMock, officeRepositoryMock);
    }

    @Test
    void addEmployeeWhenOfficeNotFound() {
        lenient().when(officeRepositoryMock.findById(ID))
                .thenReturn(empty());

        officeService.addEmployee(ID, userServiceModelMock);

        verifyNoInteractions(userServiceMock);
        verify(officeRepositoryMock, never()).saveAndFlush(any(Office.class));
    }

    @Test
    void createOffice() {
        officeService.createOffice(modelMapper.map(officeMock, OfficeServiceModel.class));
//        officeService.createOffice(officeMock);

        verify(officeRepositoryMock).saveAndFlush(officeMock);
    }

    @Test
    void getOffices() {
        when(officeRepositoryMock.findAll()).thenReturn(singletonList(officeMock));
        when(mapperMock.mapCollection(singletonList(officeMock), OfficeServiceModel.class))
                .thenReturn(singletonList(officeServiceModelMock));

        assertThat(officeService.getOffices(), equalTo(singletonList(officeServiceModelMock)));
    }

    @Test
    void testRemoveOffice() {
        officeService.removeOffice(ID);

        verify(officeRepositoryMock).deleteById(ID);
    }

    @Test
    void testFindOfficeByIdWhenOfficeNotFound() {
        when(officeRepositoryMock.findById(ID)).thenReturn(empty());

        assertThat(officeService.findOfficeById(ID), equalTo(empty()));
    }

    @Test
    void testFindOfficeById() {
        assertThat(officeService.findOfficeById(ID), equalTo(of(officeServiceModelMock)));
    }

    @Test
    void testFindOfficeByAddressWhenOfficeNotFound() {
        when(officeRepositoryMock.findByAddress(ADDRESS)).thenReturn(empty());

        assertThat(officeService.findOfficeByAddress(ADDRESS), equalTo(empty()));
    }

    @Test
    void testFindOfficeByAddress() {
        assertThat(officeService.findOfficeByAddress(ADDRESS), equalTo(of(officeServiceModelMock)));
    }
}
