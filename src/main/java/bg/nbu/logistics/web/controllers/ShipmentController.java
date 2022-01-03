package bg.nbu.logistics.web.controllers;

import static bg.nbu.logistics.commons.constants.AuthorizationConstants.IS_AUTHENTICATED;
import static java.util.stream.Collectors.toUnmodifiableList;

import java.security.Principal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import bg.nbu.logistics.domain.models.view.ShipmentViewModel;
import bg.nbu.logistics.services.shipments.ShipmentService;

@Controller
public class ShipmentController extends BaseController {
    public static final String SHIPMENTS = "all_shipments";
    public static final String MY_SHIPMENTS = "my_shipments";
    
    public static final String DELETE = "/delete";
    
    private final ShipmentService shipmentService;
    private final ModelMapper modelMapper;

    @Autowired
    public ShipmentController(ShipmentService shipmentService, ModelMapper modelMapper) {
        this.shipmentService = shipmentService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/shipments")
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView fetchAll(ModelAndView modelAndView) {
        final List<ShipmentViewModel> shipments = shipmentService.findAllShipments()
                .stream()
                .map(shipment -> modelMapper.map(shipment, ShipmentViewModel.class))
                .collect(toUnmodifiableList());
        modelAndView.addObject("shipmentViewModels", shipments);

        return view(SHIPMENTS, modelAndView);
    }

    @GetMapping("/my_shipments")
    @PreAuthorize(IS_AUTHENTICATED)    
    public ModelAndView fetchUserShipments(final Principal principal,ModelAndView modelAndView) {
        return view(MY_SHIPMENTS, modelAndView);
    }

    @DeleteMapping(DELETE + "/shipments/{id}")
    @PreAuthorize(IS_AUTHENTICATED)  
    public ModelAndView delete(@PathVariable(name = "id") long id) {
        shipmentService.delete(id);
        
        return redirect(SHIPMENTS);
    }
}
