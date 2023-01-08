package com.example.logistics.web.controllers;

import static com.example.logistics.commons.constants.AuthorizationConstants.IS_AUTHENTICATED;
import static com.example.logistics.commons.constants.paths.PathParamConstants.ID;
import static com.example.logistics.commons.constants.paths.ShipmentPathParamConstants.SHIPMENTS;
import static com.example.logistics.commons.constants.views.ShipmentViewConstants.ALL_SHIPMENTS;
import static com.example.logistics.commons.constants.views.ShipmentViewConstants.CREATE_SHIPMENT;
import static com.example.logistics.commons.constants.views.ShipmentViewConstants.EDIT_SHIPMENT;
import static com.example.logistics.commons.constants.views.ShipmentViewConstants.MY_SHIPMENTS;
import static com.example.logistics.commons.constants.views.ShipmentViewConstants.RECEIVED_SHIPMENT_VIEW_MODELS;
import static com.example.logistics.commons.constants.views.ShipmentViewConstants.SENT_SHIPMENT_VIEW_MODELS;
import static com.example.logistics.commons.constants.views.ShipmentViewConstants.SHIPMENT_VIEW_MODELS;

import java.security.Principal;
import java.util.List;

import com.example.logistics.domain.entities.Shipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.logistics.commons.constants.paths.ShipmentPathParamConstants;
import com.example.logistics.commons.utils.Mapper;
import com.example.logistics.domain.models.service.ShipmentServiceModel;
import com.example.logistics.domain.models.view.ShipmentViewModel;
import com.example.logistics.services.shipments.ShipmentService;

@Controller()
@RequestMapping(SHIPMENTS)
public class ShipmentController extends BaseController {
    private final ShipmentService shipmentService;
    private final Mapper mapper;

    @Autowired
    public ShipmentController(ShipmentService shipmentService, Mapper mapper) {
        this.shipmentService = shipmentService;
        this.mapper = mapper;
    }

    @GetMapping()
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView fetchAll(ModelAndView modelAndView) {
        modelAndView.addObject(SHIPMENT_VIEW_MODELS,
                mapper.mapCollection(shipmentService.findAllShipments(), ShipmentViewModel.class));

        return view(ALL_SHIPMENTS, modelAndView);
    }
    
    @GetMapping(ShipmentPathParamConstants.MY_SHIPMENTS)
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView fetchUserShipments(final Principal principal, ModelAndView modelAndView) {
        final List<ShipmentViewModel> sentShipments = mapper.mapCollection(
                shipmentService.findAllSentShipmentsByUsername(principal.getName()), ShipmentViewModel.class);
        final List<ShipmentViewModel> receivedShipments = mapper.mapCollection(
                shipmentService.findAllReceivedShipmentsByUsername(principal.getName()), ShipmentViewModel.class);

        modelAndView.addObject(SENT_SHIPMENT_VIEW_MODELS, sentShipments);
        modelAndView.addObject(RECEIVED_SHIPMENT_VIEW_MODELS, receivedShipments);

        return view(MY_SHIPMENTS, modelAndView);
    }

    @RequestMapping(ShipmentPathParamConstants.CREATE_SHIPMENT)
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView createShipment(final Principal principal, ModelAndView modelAndView) {
        Shipment shipment = new Shipment();
        shipment.setSender(principal.getName());
        modelAndView.addObject("shipment", shipment);
        return view(CREATE_SHIPMENT, modelAndView);
    }

    @PostMapping(ShipmentPathParamConstants.CREATE_SHIPMENT)
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView saveShipment(@ModelAttribute("shipment") Shipment shipment, ModelAndView modelAndView) {
        shipmentService.createShipment(shipment);
        modelAndView.setViewName("redirect:/" + SHIPMENTS);
        return modelAndView;
    }

    @RequestMapping(ShipmentPathParamConstants.EDIT_SHIPMENT + ID)
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView editShipment(@PathVariable(name = "id") long id, ModelAndView modelAndView) {
        ShipmentServiceModel shipment = shipmentService.findShipmentById(id);
        modelAndView.addObject("shipment", shipment);
        return view(EDIT_SHIPMENT, modelAndView);
    }

    @RequestMapping(ShipmentPathParamConstants.EDIT_SHIPMENT)
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView updateShipment(@ModelAttribute("shipment") ShipmentServiceModel shipmentServiceModel, ModelAndView modelAndView) {
        Shipment shipment = new Shipment();
        shipment.setId(shipmentServiceModel.getId());
        shipment.setSender(shipmentServiceModel.getSender());
        shipment.setRecipient(shipmentServiceModel.getRecipient());
        shipment.setAddress(shipmentServiceModel.getAddress());
        shipment.setWeight(shipmentServiceModel.getWeight());
        shipmentService.updateExistingShipment(shipment);
        modelAndView.setViewName("redirect:/" + SHIPMENTS);
        return modelAndView;
    }


    @GetMapping("/delete" + ID)
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView delete(ModelAndView modelAndView, @PathVariable(name = "id") long id) {
        shipmentService.delete(id);

        modelAndView.setViewName("redirect:/" + SHIPMENTS);
        return modelAndView;
    }
}
