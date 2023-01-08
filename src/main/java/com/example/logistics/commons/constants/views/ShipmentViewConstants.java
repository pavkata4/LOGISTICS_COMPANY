package com.example.logistics.commons.constants.views;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class ShipmentViewConstants {
    public static final String ALL_SHIPMENTS = "all_shipments";
    public static final String MY_SHIPMENTS = "my_shipments";

    public static final String CREATE_SHIPMENT = "add_shipment";
    public static final String EDIT_SHIPMENT = "edit_shipment";
    
    public static final String SHIPMENT_VIEW_MODELS = "shipmentViewModels";
    public static final String RECEIVED_SHIPMENT_VIEW_MODELS = "receivedShipmentViewModels";
    public static final String SENT_SHIPMENT_VIEW_MODELS = "sentShipmentViewModels";
}
