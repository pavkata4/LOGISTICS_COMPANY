package bg.nbu.logistics.web.controllers;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.List;

import bg.nbu.logistics.domain.entities.Shipment;
import bg.nbu.logistics.repositories.ShipmentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping("/shipments")
    @GetMapping
    public ModelAndView fetchAll(ModelAndView modelAndView) {
        final List<ShipmentViewModel> shipments = shipmentService.findAllShipments()
                .stream()
                .map(shipment -> modelMapper.map(shipment, ShipmentViewModel.class))
                .collect(toUnmodifiableList());
        modelAndView.addObject("shipmentViewModels", shipments);

        return view(SHIPMENTS, modelAndView);
    }

    @RequestMapping("/my_shipments")
    @GetMapping
    public ModelAndView fetchUserShipments(ModelAndView modelAndView) {
        final List<ShipmentViewModel> shipments = shipmentService.findAllShipments()
                .stream()
                .map(shipment -> modelMapper.map(shipment, ShipmentViewModel.class))
                .collect(toUnmodifiableList());
        modelAndView.addObject("shipmentViewModels", shipments);

        return view(MY_SHIPMENTS, modelAndView);
    }

//    @RequestMapping
//    public String fetchAll(Model model)  {
//        final List<ShipmentViewModel> shipments = shipmentService.findAllShipments()
//                .stream()
//                .map(shipment -> modelMapper.map(shipment, ShipmentViewModel.class))
//                .collect(toUnmodifiableList());
//        model.addAttribute("shipmentsList", shipments);
//
//        return SHIPMENTS;
//    }

    @DeleteMapping(DELETE + "/shipments/{id}")
    public ModelAndView delete(@PathVariable(name = "id") long id) {
        shipmentService.delete(id);
        
        return redirect(SHIPMENTS);
    }
}
