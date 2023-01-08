package com.example.logistics.domain.models.service;

import java.time.LocalDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentServiceModel extends BaseServiceModel {
    @NotEmpty
    @NotNull
    private String sender;

    @NotEmpty
    @NotNull
    private String recipient;

    @NotEmpty
    @NotNull
    private String address;
    
    @Min(value = 1, message = "Minimum weight is 1")
    private int weight;
    
    @Min(value = 1, message = "Minimum price is 1")
    private double price;
    
    @NotNull
    private LocalDate sendDate;
}