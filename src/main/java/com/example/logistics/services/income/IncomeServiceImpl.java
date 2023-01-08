package com.example.logistics.services.income;

import static java.util.stream.Collectors.summingDouble;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.logistics.domain.models.service.ShipmentServiceModel;
import com.example.logistics.services.shipments.ShipmentService;

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
                    String.format("The provided 'from' date: %s is after the 'to' date: %s", from, to));
        }

        return shipmentService.findAllShipmentsByTimePeriod(from, to)
                .stream()
                .map(ShipmentServiceModel::getPrice)
                .collect(summingDouble(price -> price));
    }
}
