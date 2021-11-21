package bg.nbu.logistics.web.controllers;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import bg.nbu.logistics.domain.models.view.ShipmentViewModel;
import bg.nbu.logistics.services.shipments.ShipmentService;

@Controller
@RequestMapping("/shipments")
public class ShipmentController extends BaseController {
    private static final String ALL_SHIPMENTS = "shipments/all_shipments";
    
    public static final String DELETE = "/delete";
    
    private final ShipmentService shipmentService;
    private final ModelMapper modelMapper;

    @Autowired
    public ShipmentController(ShipmentService shipmentService, ModelMapper modelMapper) {
        this.shipmentService = shipmentService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ModelAndView fetchAll(ModelAndView modelAndView) {
        final List<ShipmentViewModel> shipments = shipmentService.findAllShipments()
                .stream()
                .map(shipment -> modelMapper.map(shipment, ShipmentViewModel.class))
                .collect(toUnmodifiableList());
        modelAndView.addObject("shipmentViewModels", shipments);

        return view(ALL_SHIPMENTS, modelAndView);
    }
    
    @DeleteMapping(DELETE + "/{id}")
    public ModelAndView delete(@PathVariable(name = "id") long id) {
        shipmentService.delete(id);
        
        return redirect(ALL_SHIPMENTS);
    }
}
