package bg.nbu.logistics.services.shipments;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import bg.nbu.logistics.commons.utils.Mapper;
import bg.nbu.logistics.domain.entities.Shipment;
import bg.nbu.logistics.domain.models.service.ShipmentServiceModel;
import bg.nbu.logistics.repositories.ShipmentRepository;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceImplTest {
    private static final String USERNAME = "username";
    private static final long ID = 1;

    @InjectMocks
    private ShipmentServiceImpl shipmentService;

    @Mock
    private ShipmentRepository shipmentRepositoryMock;

    @Mock
    private Mapper mapperMock;

    @Mock
    private Shipment shipmentMock;

    @Mock
    private ShipmentServiceModel shipmentServiceModelMock;

    @Test
    void testFindAllShipments() {
        when(shipmentRepositoryMock.findAll()).thenReturn(singletonList(shipmentMock));
        when(mapperMock.mapCollection(singletonList(shipmentMock), ShipmentServiceModel.class))
                .thenReturn(singletonList(shipmentServiceModelMock));

        assertThat(shipmentService.findAllShipments(), contains(shipmentServiceModelMock));
    }

    @Test
    void testFindAllReceivedShipmentsByUsername() {
        when(shipmentRepositoryMock.findAllByRecipient(USERNAME)).thenReturn(singletonList(shipmentMock));
        when(mapperMock.mapCollection(singletonList(shipmentMock), ShipmentServiceModel.class))
                .thenReturn(singletonList(shipmentServiceModelMock));

        assertThat(shipmentService.findAllReceivedShipmentsByUsername(USERNAME), contains(shipmentServiceModelMock));
    }

    @Test
    void testFindAllSentShipmentsByUsername() {
        when(shipmentRepositoryMock.findAllBySender(USERNAME)).thenReturn(singletonList(shipmentMock));
        when(mapperMock.mapCollection(singletonList(shipmentMock), ShipmentServiceModel.class))
                .thenReturn(singletonList(shipmentServiceModelMock));

        assertThat(shipmentService.findAllSentShipmentsByUsername(USERNAME), contains(shipmentServiceModelMock));
    }

    @Test
    void testDelete() {
        shipmentService.delete(ID);

        verify(shipmentRepositoryMock).deleteById(ID);
    }
}
