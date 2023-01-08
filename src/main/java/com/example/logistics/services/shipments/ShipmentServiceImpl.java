package com.example.logistics.services.shipments;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.logistics.commons.utils.Mapper;
import com.example.logistics.domain.entities.Shipment;
import com.example.logistics.domain.models.service.OfficeServiceModel;
import com.example.logistics.domain.models.service.ShipmentServiceModel;
import com.example.logistics.repositories.ShipmentRepository;
import com.example.logistics.services.offices.OfficeService;

@Service
public class ShipmentServiceImpl implements ShipmentService {
    public static final double PERSONAL_ADDRESS_PRICE_MULTIPLIER = 1.5;
    
    private final ShipmentRepository shipmentRepository;
    private final Mapper mapper;
    private final OfficeService officeService;

    @Autowired
    public ShipmentServiceImpl(ShipmentRepository shipmentRepository, OfficeService officeService, Mapper mapper) {
        this.shipmentRepository = shipmentRepository;
        this.officeService = officeService;
        this.mapper = mapper;
    }

    @Override
    public List<ShipmentServiceModel> findAllShipments() {
        return mapper.mapCollection(shipmentRepository.findAll(), ShipmentServiceModel.class);
    }

    @Override
    public List<ShipmentServiceModel> findAllReceivedShipmentsByUsername(final String username) {
        return mapper.mapCollection(shipmentRepository.findAllByRecipient(username), ShipmentServiceModel.class);
    }

    @Override
    public List<ShipmentServiceModel> findAllSentShipmentsByUsername(final String username) {
        return mapper.mapCollection(shipmentRepository.findAllBySender(username), ShipmentServiceModel.class);
    }

    @Override
    public ShipmentServiceModel findShipmentById(long id) {
        Shipment shipment = shipmentRepository.findById(id).get();
        ShipmentServiceModel shipmentServiceModel = new ShipmentServiceModel();
        shipmentServiceModel.setId(shipment.getId());
        shipmentServiceModel.setSender(shipment.getSender());
        shipmentServiceModel.setRecipient(shipment.getRecipient());
        shipmentServiceModel.setAddress(shipment.getAddress());
        shipmentServiceModel.setWeight(shipment.getWeight());
        return shipmentServiceModel;
    }

    @Override
    public Shipment createShipment(Shipment shipment) {
        final Optional<OfficeServiceModel> office = officeService.findOfficeByAddress(shipment.getAddress());
        shipment.setPrice(calculatePrice(office,shipment));
        
        return shipmentRepository.saveAndFlush(shipment);
    }

    @Override
    public Shipment updateExistingShipment(Shipment shipment) {
        return shipmentRepository.findById(shipment.getId())
                .map(updateShipment -> {
                    return shipmentRepository.save(shipment);
                })
                .get();
    }
    
    @Override
    public void delete(final long id) {
        shipmentRepository.deleteById(id);
    }

    @Override
    public List<ShipmentServiceModel> findAllShipmentsByTimePeriod(LocalDate from, LocalDate to) {
        return mapper.mapCollection(shipmentRepository.findBySendDateBetween(from, to), ShipmentServiceModel.class);
    }

    private double calculatePrice(Optional<OfficeServiceModel> office, Shipment shipment) {
        if (office.isEmpty()) {
            return shipment.getWeight() * PERSONAL_ADDRESS_PRICE_MULTIPLIER;
        }
        return shipment.getWeight();
    }
}