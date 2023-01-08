package com.example.logistics.services.income;

import static java.time.LocalDate.now;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.logistics.domain.models.service.ShipmentServiceModel;
import com.example.logistics.services.shipments.ShipmentService;

@ExtendWith(MockitoExtension.class)
class IncomeServiceImplTest {
    private static final double PRICE = 10;

    @InjectMocks
    private IncomeServiceImpl incomeService;

    @Mock
    private ShipmentService shipmentServiceMock;

    @Mock
    private ShipmentServiceModel shipmentServiceModelMock;

    @Test
    void testGetIncomeByTimePeriodThrowsExceptionWhenFromDateIsAfterToDate() {
        assertThrows(IllegalStateException.class, () -> incomeService.getIncomeByTimePeriod(now(), now().minusDays(5)));
    }

    @Test
    void testGetIncomeByTimePeriodWhenNoShipmentsInPeriod() {
        when(shipmentServiceMock.findAllShipmentsByTimePeriod(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.<ShipmentServiceModel>emptyList());

        assertThat(incomeService.getIncomeByTimePeriod(now(), now().plusWeeks(2)), equalTo(0d));
    }

    @Test
    void testGetIncomeByTimePeriod() {
        when(shipmentServiceMock.findAllShipmentsByTimePeriod(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(singletonList(shipmentServiceModelMock));
        when(shipmentServiceModelMock.getPrice()).thenReturn(PRICE);

        assertThat(incomeService.getIncomeByTimePeriod(now(), now().plusWeeks(2)), equalTo(PRICE));
    }
}
