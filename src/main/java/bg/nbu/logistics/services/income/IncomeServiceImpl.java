package bg.nbu.logistics.services.income;

import java.time.LocalDate;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bg.nbu.logistics.domain.models.service.ShipmentServiceModel;
import bg.nbu.logistics.services.shipments.ShipmentService;

@Service
public class IncomeServiceImpl implements IncomeService {
    private final ShipmentService shipmentService;

    @Autowired
    public IncomeServiceImpl(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    public double getIncomeByTimePeriod(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new IllegalStateException(
                    String.format("The prodided 'from' date: %s is after the 'to' date: %s", from, to));
        }

        return shipmentService.findAllShipmentsByTimePeriod(from, to)
                .stream()
                .filter(isShipmentSendBetweenDates(from, to))
                .map(ShipmentServiceModel::getPrice)
                .mapToDouble(price -> price)
                .sum();
    }

    private Predicate<ShipmentServiceModel> isShipmentSendBetweenDates(LocalDate from, LocalDate to) {
        return shipment -> shipment.getSendDate()
                .isAfter(from)
                && shipment.getSendDate()
                        .isBefore(to);
    }
}
