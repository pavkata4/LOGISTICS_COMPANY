package bg.nbu.logistics.services.shipments;

import java.util.List;

import bg.nbu.logistics.domain.models.service.ShipmentServiceModel;

public interface ShipmentService {
    List<ShipmentServiceModel> findAllShipments();

    void delete(long id);

    List<ShipmentServiceModel> findAllReceivedShipmentsByUsername(String username);

    List<ShipmentServiceModel> findAllSentShipmentsByUsername(String username);
}
