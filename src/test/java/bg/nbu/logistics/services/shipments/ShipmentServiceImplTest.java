package bg.nbu.logistics.services.shipments;

import static bg.nbu.logistics.services.shipments.ShipmentServiceImpl.PERSONAL_ADDRESS_PRICE_MULTIPLIER;
import static java.util.Collections.singletonList;
import static java.util.Optional.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import bg.nbu.logistics.commons.utils.Mapper;
import bg.nbu.logistics.domain.entities.Shipment;
import bg.nbu.logistics.domain.models.service.OfficeServiceModel;
import bg.nbu.logistics.domain.models.service.ShipmentServiceModel;
import bg.nbu.logistics.repositories.ShipmentRepository;
import bg.nbu.logistics.services.offices.OfficeService;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceImplTest {
    private static final int WEIGHT = 10;
    private static final String ADDRESS = "address";
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

    @Mock
    private OfficeService officeServiceMock;
    
    @Mock
    private OfficeServiceModel officeServiceModelMock;
    
    @BeforeEach
    public void setUp() {
        lenient().when(shipmentMock.getAddress()).thenReturn(ADDRESS);
        lenient().when(shipmentMock.getWeight()).thenReturn(WEIGHT);
        lenient().when(shipmentRepositoryMock.saveAndFlush(shipmentMock)).thenReturn(shipmentMock);
        
    }
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
    
    @Test
    void testCreateShipmentToOffice() {
        when(officeServiceMock.findOfficeByAddress(ADDRESS)).thenReturn(of(officeServiceModelMock));

        assertThat(shipmentService.createShipment(shipmentMock), equalTo(shipmentMock));

        verify(shipmentMock).setPrice(WEIGHT);
        verify(shipmentRepositoryMock).saveAndFlush(shipmentMock);
    }
    
    @Test
    void testCreateNewShipmentToPersonalAddress() {
        when(officeServiceMock.findOfficeByAddress(ADDRESS)).thenReturn(Optional.<OfficeServiceModel>empty());

        assertThat(shipmentService.createShipment(shipmentMock), equalTo(shipmentMock));

        verify(shipmentMock).setPrice(WEIGHT * PERSONAL_ADDRESS_PRICE_MULTIPLIER);
        verify(shipmentRepositoryMock).saveAndFlush(shipmentMock);
    }
}
