package com.example.logistics.services.shipments;

import java.time.LocalDate;
import java.util.List;

import com.example.logistics.domain.entities.Shipment;
import com.example.logistics.domain.models.service.ShipmentServiceModel;

public interface ShipmentService {
    List<ShipmentServiceModel> findAllShipments();

    void delete(long id);

    List<ShipmentServiceModel> findAllReceivedShipmentsByUsername(String username);

    List<ShipmentServiceModel> findAllSentShipmentsByUsername(String username);

    ShipmentServiceModel findShipmentById(long id);

    Shipment createShipment(Shipment shipment);

    Shipment updateExistingShipment(Shipment shipment);

    List<ShipmentServiceModel> findAllShipmentsByTimePeriod(LocalDate from, LocalDate to);
}
