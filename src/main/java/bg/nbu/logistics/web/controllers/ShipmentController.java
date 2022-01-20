package bg.nbu.logistics.web.controllers;

import static bg.nbu.logistics.commons.constants.AuthorizationConstants.IS_AUTHENTICATED;
import static bg.nbu.logistics.commons.constants.paths.ShipmentPathParamConstants.SHIPMENTS;
import static bg.nbu.logistics.commons.constants.views.ShipmentViewConstants.ALL_SHIPMENTS;
import static bg.nbu.logistics.commons.constants.views.ShipmentViewConstants.MY_SHIPMENTS;
import static bg.nbu.logistics.commons.constants.views.ShipmentViewConstants.RECEIVED_SHIPMENT_VIEW_MODELS;
import static bg.nbu.logistics.commons.constants.views.ShipmentViewConstants.SENT_SHIPMENT_VIEW_MODELS;
import static bg.nbu.logistics.commons.constants.views.ShipmentViewConstants.SHIPMENT_VIEW_MODELS;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import bg.nbu.logistics.commons.constants.paths.ShipmentPathParamConstants;
import bg.nbu.logistics.commons.utils.Mapper;
import bg.nbu.logistics.domain.models.view.ShipmentViewModel;
import bg.nbu.logistics.services.shipments.ShipmentService;

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

    @GetMapping("/{id}/delete")
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView delete(@PathVariable(name = "id") long id) {
        shipmentService.delete(id);

        return redirect(ALL_SHIPMENTS);
    }
}
