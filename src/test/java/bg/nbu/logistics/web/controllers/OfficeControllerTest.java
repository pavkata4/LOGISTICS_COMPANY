package bg.nbu.logistics.web.controllers;

import static bg.nbu.logistics.commons.constants.RoleConstants.EMPLOYEE;
import static bg.nbu.logistics.commons.constants.paths.OfficePathParamConstants.MANAGER;
import static bg.nbu.logistics.commons.constants.paths.OfficePathParamConstants.OFFICES;
import static bg.nbu.logistics.commons.constants.views.OfficeViewConstants.ADD_OFFICE;
import static bg.nbu.logistics.commons.constants.views.OfficeViewConstants.ALL_OFFICES;
import static bg.nbu.logistics.commons.constants.views.OfficeViewConstants.OFFICE_LIST_VIEW_MODELS;
import static bg.nbu.logistics.commons.constants.views.OfficeViewConstants.OFFICE_VIEW_MODEL;
import static java.util.Collections.singleton;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.boot.jdbc.EmbeddedDatabaseConnection.H2;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import bg.nbu.logistics.domain.models.service.OfficeServiceModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriBuilder;

import bg.nbu.logistics.domain.entities.Office;
import bg.nbu.logistics.domain.entities.User;
import bg.nbu.logistics.domain.models.service.RoleServiceModel;
import bg.nbu.logistics.domain.models.service.UserServiceModel;
import bg.nbu.logistics.services.offices.OfficeService;
import bg.nbu.logistics.services.users.UserService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = H2)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class OfficeControllerTest {
    private static final String EMPLOYEES = "employees";
    private static final String ADDRESS = "address";
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UriBuilder uriBuilder;

    @Autowired
    private OfficeService officeService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        final User user = modelMapper.map(
                userService
                        .register(new UserServiceModel(USERNAME, PASSWORD, singleton(new RoleServiceModel(EMPLOYEE)))),
                User.class);

        officeService.createOffice(modelMapper.map(new Office(ADDRESS, singleton(user)), OfficeServiceModel.class));
    }

    @Test
    @WithMockUser
    void testFetchAll() throws Exception {
        mockMvc.perform(get(uriBuilder.pathSegment(OFFICES)
                .build()))
                .andExpect(model().attribute(OFFICE_LIST_VIEW_MODELS,
                        hasItem(allOf(hasProperty(ADDRESS, equalTo(ADDRESS)),
                                hasProperty(EMPLOYEES, everyItem(hasProperty(USERNAME, equalTo(USERNAME))))))))
                .andExpect(view().name(ALL_OFFICES));
    }

    @Test
    @WithMockUser
    void testAddOffice() throws Exception {
        mockMvc.perform(get(uriBuilder.pathSegment(OFFICES, MANAGER)
                .build()))
                .andExpect(model().attributeExists(OFFICE_VIEW_MODEL))
                .andExpect(view().name(ADD_OFFICE));
    }
}
