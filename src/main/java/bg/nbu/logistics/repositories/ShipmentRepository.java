package bg.nbu.logistics.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import bg.nbu.logistics.domain.entities.Shipment;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
}
