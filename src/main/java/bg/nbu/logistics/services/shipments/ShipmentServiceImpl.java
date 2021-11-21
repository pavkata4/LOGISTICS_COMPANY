package bg.nbu.logistics.services.shipments;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bg.nbu.logistics.domain.models.service.ShipmentServiceModel;
import bg.nbu.logistics.repositories.ShipmentRepository;

@Service
public class ShipmentServiceImpl implements ShipmentService {
    private final ShipmentRepository shipmentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ShipmentServiceImpl(ShipmentRepository shipmentRepository, ModelMapper modelMapper) {
        this.shipmentRepository = shipmentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ShipmentServiceModel> findAllShipments() {
        return shipmentRepository.findAll()
                .stream()
                .map(shipment -> modelMapper.map(shipment, ShipmentServiceModel.class))
                .collect(toUnmodifiableList());
    }

    @Override
    public void delete(long id) {
        shipmentRepository.deleteById(id);
    }
}
