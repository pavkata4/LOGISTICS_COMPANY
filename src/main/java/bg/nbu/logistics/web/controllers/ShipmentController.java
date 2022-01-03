package bg.nbu.logistics.web.controllers;

import static bg.nbu.logistics.commons.constants.AuthorizationConstants.IS_AUTHENTICATED;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import bg.nbu.logistics.commons.utils.Mapper;
import bg.nbu.logistics.domain.models.view.ShipmentViewModel;
import bg.nbu.logistics.services.shipments.ShipmentService;

@Controller
public class ShipmentController extends BaseController {
    public static final String SHIPMENTS = "all_shipments";
    public static final String MY_SHIPMENTS = "my_shipments";

    public static final String DELETE = "/delete";

    private final ShipmentService shipmentService;
    private final Mapper mapper;

    @Autowired
    public ShipmentController(ShipmentService shipmentService, Mapper mapper) {
        this.shipmentService = shipmentService;
        this.mapper = mapper;
    }

    @GetMapping("/shipments")
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView fetchAll(ModelAndView modelAndView) {
        modelAndView.addObject("shipmentViewModels",
                mapper.mapCollection(shipmentService.findAllShipments(), ShipmentViewModel.class));

        return view(SHIPMENTS, modelAndView);
    }

    @GetMapping("/my_shipments")
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView fetchUserShipments(final Principal principal, ModelAndView modelAndView) {
        final List<ShipmentViewModel> sentShipments = mapper.mapCollection(
                shipmentService.findAllSentShipmentsByUsername(principal.getName()), ShipmentViewModel.class);
        final List<ShipmentViewModel> receivedShipments = mapper.mapCollection(
                shipmentService.findAllReceivedShipmentsByUsername(principal.getName()), ShipmentViewModel.class);

        modelAndView.addObject("sentShipmentViewModels", sentShipments);
        modelAndView.addObject("receivedShipmentViewModels", receivedShipments);

        return view(MY_SHIPMENTS, modelAndView);
    }

    @DeleteMapping(DELETE + "/shipments/{id}")
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView delete(@PathVariable(name = "id") long id) {
        shipmentService.delete(id);

        return redirect(SHIPMENTS);
    }
}
