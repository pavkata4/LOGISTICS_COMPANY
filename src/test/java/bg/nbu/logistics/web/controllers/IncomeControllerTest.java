package bg.nbu.logistics.web.controllers;

import static bg.nbu.logistics.commons.constants.paths.IncomePathParamConstants.INCOME_PATH;
import static bg.nbu.logistics.commons.constants.paths.IncomePathParamConstants.IN_RANGE;
import static bg.nbu.logistics.commons.constants.views.IncomeViewConstants.INCOME;
import static bg.nbu.logistics.commons.constants.views.IncomeViewConstants.INCOME_VIEW_MODEL;
import static java.time.LocalDate.now;
import static org.springframework.boot.jdbc.EmbeddedDatabaseConnection.H2;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriBuilder;

import bg.nbu.logistics.domain.entities.Shipment;
import bg.nbu.logistics.services.income.IncomeService;
import bg.nbu.logistics.services.shipments.ShipmentService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = H2)
class IncomeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UriBuilder uriBuilder;

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private ShipmentService shipmentService;

    @Test
    void testGetIncomeView() throws Exception {
        mockMvc.perform(get(INCOME_PATH))
                .andExpect(view().name(INCOME));
    }

    @Test
    void testFetchIncomeInRange() throws Exception {
        shipmentService.createShipment(new Shipment("sender", "recipient", "address", 5, 10, now()));

        mockMvc.perform(get(uriBuilder.pathSegment(INCOME_PATH, IN_RANGE)
                .build()))
                .andExpect(model().attribute(INCOME_VIEW_MODEL, incomeService.getIncomeByTimePeriod(now(), now())))
                .andExpect(view().name(INCOME));
    }
}
