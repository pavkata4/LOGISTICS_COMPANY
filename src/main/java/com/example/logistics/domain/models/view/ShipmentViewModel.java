package com.example.logistics.domain.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ShipmentViewModel extends BaseViewModel {
    private String sender;
    private String recipient;
    private String address;
    private int weight;
    private double price;
    private Date date;
}