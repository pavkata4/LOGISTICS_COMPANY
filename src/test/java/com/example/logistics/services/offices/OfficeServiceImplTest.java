package bg.nbu.logistics.services.offices;

import static bg.nbu.logistics.commons.constants.RoleConstants.ROLE_USER;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
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

    @BeforeEach
    void setUp() {
        lenient().when(mapperMock.mapCollection(singletonList(officeMock), OfficeServiceModel.class))
                .thenReturn(singletonList(officeServiceModelMock));
        lenient().when(modelMapperMock.map(officeMock, OfficeServiceModel.class))
                .thenReturn(officeServiceModelMock);
        lenient().when(modelMapperMock.map(userServiceModelMock, User.class))
                .thenReturn(userMock);
        lenient().when(modelMapperMock.map(officeServiceModelMock, Office.class))
                .thenReturn(officeMock);

        lenient().when(officeRepositoryMock.findById(ID))
                .thenReturn(of(officeMock));
        lenient().when(officeRepositoryMock.findByAddress(ADDRESS))
                .thenReturn(of(officeMock));
        lenient().when(officeRepositoryMock.saveAndFlush(officeMock))
                .thenReturn(officeMock);

        lenient().when(userServiceModelMock.getAuthorities())
                .thenReturn(singleton(roleServiceModelMock));

        lenient().when(roleServiceModelMock.getAuthority())
                .thenReturn(ROLE_USER);

        lenient().when(officeMock.getEmployees())
                .thenReturn(newSet(userMock));
        lenient().when(officeMock.getId())
                .thenReturn(ID);
        lenient().when(officeMock.getAddress())
                .thenReturn(ADDRESS);
    }

    @Test
    void createOffice() {
        assertThat(officeService.createOffice(officeServiceModelMock), equalTo(officeServiceModelMock));

        verify(officeMock).setId(ID);
        verify(officeMock).setAddress(ADDRESS);
        verify(officeMock).setEmployees(singleton(userMock));
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
