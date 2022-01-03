package bg.nbu.logistics.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import bg.nbu.logistics.domain.entities.Shipment;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    List<Shipment> findAllByRecipient(String recipient);
    List<Shipment> findAllBySender(String sender);
}
