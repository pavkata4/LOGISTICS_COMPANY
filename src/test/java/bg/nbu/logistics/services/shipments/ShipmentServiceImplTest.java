package bg.nbu.logistics.services.shipments;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import bg.nbu.logistics.domain.entities.Shipment;
import bg.nbu.logistics.domain.models.service.ShipmentServiceModel;
import bg.nbu.logistics.repositories.ShipmentRepository;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceImplTest {
    private static final long ID = 1;

    @InjectMocks
    private ShipmentServiceImpl shipmentService;

    @Mock
    private ShipmentRepository shipmentRepositoryMock;

    @Mock
    private ModelMapper modelMapperMock;

    @Mock
    private Shipment shipmentMock;

    @Mock
    private ShipmentServiceModel shipmentServiceModelMock;

    @Test
    void testFindAllShipmentsNoShipmentsPresent() {
        when(shipmentRepositoryMock.findAll()).thenReturn(emptyList());

        assertThat(shipmentService.findAllShipments(), empty());
    }

    @Test
    void testFindAllShipments() {
        when(shipmentRepositoryMock.findAll()).thenReturn(singletonList(shipmentMock));
        when(modelMapperMock.map(shipmentMock, ShipmentServiceModel.class)).thenReturn(shipmentServiceModelMock);

        assertThat(shipmentService.findAllShipments(), contains(shipmentServiceModelMock));
    }

    @Test
    void testDelete() {
        shipmentService.delete(ID);

        verify(shipmentRepositoryMock).deleteById(ID);
    }
}
