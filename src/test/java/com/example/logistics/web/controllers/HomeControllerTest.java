package com.example.logistics.web.controllers;

import static com.example.logistics.commons.constants.paths.PathParamConstants.HOME;
import static com.example.logistics.commons.constants.paths.PathParamConstants.HOME_PATH;
import static com.example.logistics.commons.constants.paths.PathParamConstants.INDEX;
import static com.example.logistics.commons.constants.paths.PathParamConstants.INDEX_PATH;
import static org.springframework.boot.jdbc.EmbeddedDatabaseConnection.H2;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = H2)
class HomeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testIndexReturnsCorrectView() throws Exception {
        mockMvc.perform(get(INDEX_PATH))
                .andExpect(view().name(INDEX));
    }

    @Test
    @WithMockUser
    void testHomeReturnsCorrectView() throws Exception {
        mockMvc.perform(get(HOME_PATH))
                .andExpect(view().name(HOME));
    }
}