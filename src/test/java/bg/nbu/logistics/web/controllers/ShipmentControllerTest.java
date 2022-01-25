package bg.nbu.logistics.web.controllers;

import static bg.nbu.logistics.commons.constants.RoleConstants.EMPLOYEE;
import static bg.nbu.logistics.commons.constants.paths.PathParamConstants.DELETE;
import static bg.nbu.logistics.commons.constants.paths.ShipmentPathParamConstants.SHIPMENTS;
import static bg.nbu.logistics.commons.constants.views.ShipmentViewConstants.ALL_SHIPMENTS;
import static bg.nbu.logistics.commons.constants.views.ShipmentViewConstants.MY_SHIPMENTS;
import static bg.nbu.logistics.commons.constants.views.ShipmentViewConstants.RECEIVED_SHIPMENT_VIEW_MODELS;
import static bg.nbu.logistics.commons.constants.views.ShipmentViewConstants.SENT_SHIPMENT_VIEW_MODELS;
import static bg.nbu.logistics.commons.constants.views.ShipmentViewConstants.SHIPMENT_VIEW_MODELS;
import static java.util.Collections.singleton;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.boot.jdbc.EmbeddedDatabaseConnection.H2;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriBuilder;

import bg.nbu.logistics.commons.constants.paths.ShipmentPathParamConstants;
import bg.nbu.logistics.domain.entities.Shipment;
import bg.nbu.logistics.domain.models.service.RoleServiceModel;
import bg.nbu.logistics.domain.models.service.ShipmentServiceModel;
import bg.nbu.logistics.domain.models.service.UserServiceModel;
import bg.nbu.logistics.repositories.ShipmentRepository;
import bg.nbu.logistics.services.shipments.ShipmentService;
import bg.nbu.logistics.services.users.UserService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = H2)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class ShipmentControllerTest {
    private static final double PRICE = 30;
    private static final String SENDER = "sender";
    private static final String RECIPIENT = "recipient";
    private static final String ADDRESS = "address";
    private static final int WEIGHT = 10;
    private static final String WEIGHT_PROPERTY = "weight";

    private static final String PASSWORD = "password";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UriBuilder uriBuilder;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        shipmentRepository.saveAndFlush(new Shipment(SENDER, RECIPIENT, ADDRESS, WEIGHT, PRICE));
        shipmentRepository.saveAndFlush(new Shipment(RECIPIENT, SENDER, ADDRESS, WEIGHT, PRICE));
    }

    @Test
    @WithMockUser()
    void testFetchAll() throws Exception {
        mockMvc.perform(get(uriBuilder.pathSegment(SHIPMENTS)
                .build()))
                .andExpect(model().attribute(SHIPMENT_VIEW_MODELS,
                        hasItem(allOf(hasProperty(SENDER, equalTo(SENDER)), hasProperty(RECIPIENT, equalTo(RECIPIENT)),
                                hasProperty(ADDRESS, equalTo(ADDRESS)),
                                hasProperty(WEIGHT_PROPERTY, equalTo(WEIGHT))))))
                .andExpect(view().name(ALL_SHIPMENTS));
    }

    @Test
    @WithMockUser(username = SENDER)
    void testFetchUserShipments() throws Exception {
        userService.register(new UserServiceModel(SENDER, PASSWORD, singleton(new RoleServiceModel(EMPLOYEE))));

        mockMvc.perform(get(uriBuilder.pathSegment(SHIPMENTS, ShipmentPathParamConstants.MY_SHIPMENTS)
                .build()))
                .andExpectAll(
                        model().attribute(SENT_SHIPMENT_VIEW_MODELS, hasItem(allOf(hasProperty(SENDER, equalTo(SENDER)),
                                hasProperty(RECIPIENT, equalTo(RECIPIENT)), hasProperty(ADDRESS, equalTo(ADDRESS)),
                                hasProperty(WEIGHT_PROPERTY, equalTo(WEIGHT))))),
                        model().attribute(RECEIVED_SHIPMENT_VIEW_MODELS,
                                hasItem(allOf(hasProperty(SENDER, equalTo(RECIPIENT)),
                                        hasProperty(RECIPIENT, equalTo(SENDER)), hasProperty(ADDRESS, equalTo(ADDRESS)),
                                        hasProperty(WEIGHT_PROPERTY, equalTo(WEIGHT))))))
                .andExpect(view().name(MY_SHIPMENTS));
    }

    @Test
    @WithMockUser()
    void testDelete() throws Exception {
        mockMvc.perform(get(uriBuilder.pathSegment(SHIPMENTS, DELETE, Long.toString(findShipmentBySender().getId()))
                .build()))
                .andExpect(redirectedUrl("/" + SHIPMENTS));
    }

    private ShipmentServiceModel findShipmentBySender() {
        return shipmentService.findAllShipments()
                .stream()
                .filter(shipment -> SENDER.equals(shipment.getSender()))
                .findAny()
                .orElseThrow();
    }
}