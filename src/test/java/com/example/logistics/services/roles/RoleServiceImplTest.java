package com.example.logistics.services.roles;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.example.logistics.domain.entities.Role;
import com.example.logistics.domain.models.service.RoleServiceModel;
import com.example.logistics.repositories.RoleRepository;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
    private static final String AUTHORITY = "authority";

    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private RoleRepository roleRepositoryMock;

    @Mock
    private ModelMapper modelMapperMock;

    @Mock
    private RoleServiceModel roleServiceModelMock;

    private Role roleMock;

    @BeforeEach
    public void setUp() {
       lenient().when(modelMapperMock.map(roleMock, RoleServiceModel.class)).thenReturn(roleServiceModelMock);
    }

    @Test
    void testFindAllRolesNoRolesPresent() {
        when(roleRepositoryMock.findAll()).thenReturn(emptyList());

        assertThat(roleService.findAllRoles(), empty());
    }

    @Test
    void testFindAllRoles() {
        when(roleRepositoryMock.findAll()).thenReturn(singletonList(roleMock));

        assertThat(roleService.findAllRoles(), contains(roleServiceModelMock));
    }

    @Test
    void testFindByAuthority() {
        when(roleRepositoryMock.findByAuthority(AUTHORITY)).thenReturn(roleMock);

        assertThat(roleService.findByAuthority(AUTHORITY), equalTo(roleServiceModelMock));
    }
}
