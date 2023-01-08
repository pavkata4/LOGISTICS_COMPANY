package bg.nbu.logistics.commons.constants.paths;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class ShipmentPathParamConstants {
    public static final String SHIPMENTS = "shipments";
    public static final String MY_SHIPMENTS = "my-shipments";
    public static final String CREATE_SHIPMENT = "send-shipment";
    public static final String EDIT_SHIPMENT = "edit-shipment";
}
