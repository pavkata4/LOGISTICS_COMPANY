package bg.nbu.logistics.web.controllers;

import static bg.nbu.logistics.commons.constants.RoleConstants.EMPLOYEE;
import static bg.nbu.logistics.commons.constants.paths.PathParamConstants.DELETE;
import static bg.nbu.logistics.commons.constants.paths.UserPathParamConstants.LOGIN_PATH;
import static bg.nbu.logistics.commons.constants.paths.UserPathParamConstants.REGISTER_PATH;
import static bg.nbu.logistics.commons.constants.paths.UserPathParamConstants.USERS;
import static bg.nbu.logistics.commons.constants.views.UserViewConstants.ALL_USERS;
import static bg.nbu.logistics.commons.constants.views.UserViewConstants.LOGIN;
import static bg.nbu.logistics.commons.constants.views.UserViewConstants.REGISTRATION;
import static bg.nbu.logistics.commons.constants.views.UserViewConstants.USER_LIST_VIEW_MODELS;
import static bg.nbu.logistics.commons.constants.views.UserViewConstants.USER_REGISTER_BINDING_MODEL;
import static java.util.Collections.singleton;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.boot.jdbc.EmbeddedDatabaseConnection.H2;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriBuilder;

import bg.nbu.logistics.domain.models.service.RoleServiceModel;
import bg.nbu.logistics.domain.models.service.UserServiceModel;
import bg.nbu.logistics.services.users.UserService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = H2)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class UserControllerTest {
    private static final String NEW_REGISTRATION_USERNAME = "newRegistrationUsername";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UriBuilder uriBuilder;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userService.register(new UserServiceModel(USERNAME, PASSWORD, singleton(new RoleServiceModel(EMPLOYEE))));
    }

    @Test
    @WithMockUser(username = USERNAME)
    void testFetchAll() throws Exception {
        mockMvc.perform(get(uriBuilder.pathSegment(USERS)
                .build()))
                .andExpect(model().attribute(USER_LIST_VIEW_MODELS, hasItem(hasProperty(USERNAME, equalTo(USERNAME)))))
                .andExpect(view().name(ALL_USERS));
    }

    @Test
    void testRegister() throws Exception {
        mockMvc.perform(get(uriBuilder.pathSegment(USERS, REGISTER_PATH)
                .build()))
                .andExpect(model().attributeExists(USER_REGISTER_BINDING_MODEL))
                .andExpect(view().name(REGISTRATION));
    }

    @Test
    void testRegisterConfirm() throws Exception {
        final UriBuilder builder = uriBuilder.pathSegment(USERS, "{path}");

        mockMvc.perform(post(builder.build(REGISTER_PATH)).param(USERNAME, NEW_REGISTRATION_USERNAME)
                .param(PASSWORD, PASSWORD)
                .param(EMAIL, EMAIL))
                .andExpect(redirectedUrl(builder.build(LOGIN_PATH)
                        .toString()));

        final UserServiceModel userServiceModel = findUserByUsername();

        assertThat(userServiceModel.getUsername(), equalTo(USERNAME));
        assertThat(passwordEncoder.matches(PASSWORD, userServiceModel.getPassword()), equalTo(true));
    }

    @Test
    void testLogin() throws Exception {
        mockMvc.perform(get(uriBuilder.pathSegment(USERS, LOGIN_PATH)
                .build()))
                .andExpect(view().name(LOGIN));
    }

    @Test
    @WithMockUser(username = USERNAME)
    void testDelete() throws Exception {
        mockMvc.perform(get(uriBuilder.pathSegment(USERS, Long.toString(findUserByUsername().getId()), DELETE)
                .build()))
                .andExpect(redirectedUrl(USERS));
    }

    private UserServiceModel findUserByUsername() {
        return userService.findByUsername(USERNAME)
                .orElseThrow();
    }
}