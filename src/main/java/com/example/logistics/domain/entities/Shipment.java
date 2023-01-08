package com.example.logistics.domain.entities;

import static java.time.LocalDate.now;

import java.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shipments")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "sender", nullable = false)
    private String sender;

    @Column(name = "recipient", nullable = false)
    private String recipient;

    @Column(name = "address", nullable = false)
    private String address;

    @Min(value = 1, message = "Minimum weight is 1")
    private int weight;
    
    @Min(value = 1, message = "Minimum price is 1")
    private double price;
    
    @Column(name = "send_date", nullable = false, updatable = false)
    private LocalDate sendDate = now();

    public Shipment(String sender, String recipient, String address, int weight, double price, LocalDate sendDate) {
        this.sender = sender;
        this.recipient = recipient;
        this.address = address;
        this.weight = weight;
        this.price = price;
        this.sendDate = sendDate;
    }
}
