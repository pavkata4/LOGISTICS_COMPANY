package bg.nbu.logistics.services.shipments;

import java.util.List;

import bg.nbu.logistics.domain.entities.Shipment;
import bg.nbu.logistics.domain.models.service.ShipmentServiceModel;

public interface ShipmentService {
    List<ShipmentServiceModel> findAllShipments();

    void delete(long id);

    List<ShipmentServiceModel> findAllReceivedShipmentsByUsername(String username);

    List<ShipmentServiceModel> findAllSentShipmentsByUsername(String username);

    ShipmentServiceModel findShipmentById(long id);

    public Shipment createNewShipment(Shipment shipment);

    public Shipment updateExistingShipment(Shipment shipment);
}
