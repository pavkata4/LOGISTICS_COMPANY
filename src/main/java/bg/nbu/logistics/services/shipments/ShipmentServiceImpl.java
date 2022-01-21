package bg.nbu.logistics.services.shipments;

import java.util.List;

import bg.nbu.logistics.domain.entities.Shipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bg.nbu.logistics.commons.utils.Mapper;
import bg.nbu.logistics.domain.models.service.ShipmentServiceModel;
import bg.nbu.logistics.repositories.ShipmentRepository;

@Service
public class ShipmentServiceImpl implements ShipmentService {
    private final ShipmentRepository shipmentRepository;
    private final Mapper mapper;

    @Autowired
    public ShipmentServiceImpl(ShipmentRepository shipmentRepository, Mapper mapper) {
        this.shipmentRepository = shipmentRepository;
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
    public Shipment createNewShipment(Shipment shipment) {
        return shipmentRepository.save(shipment);
    }

    @Override
    public Shipment updateExistingShipment(Shipment shipment) {
        return shipmentRepository.findById(shipment.getId()).map(updateShipment->{return shipmentRepository.save(shipment);}).get();
    }
    
    @Override
    public void delete(final long id) {
        shipmentRepository.deleteById(id);
    }
}