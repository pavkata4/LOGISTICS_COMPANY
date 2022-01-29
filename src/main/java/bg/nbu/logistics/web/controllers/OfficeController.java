package bg.nbu.logistics.web.controllers;

import static bg.nbu.logistics.commons.constants.AuthorizationConstants.IS_AUTHENTICATED;
import static bg.nbu.logistics.commons.constants.AuthorizationConstants.UNABLE_TO_FIND_USER_BY_NAME_MESSAGE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import bg.nbu.logistics.commons.utils.Mapper;
import bg.nbu.logistics.domain.entities.Office;
import bg.nbu.logistics.domain.models.service.UserServiceModel;
import bg.nbu.logistics.domain.models.view.OfficeViewModel;
import bg.nbu.logistics.services.offices.OfficeService;
import bg.nbu.logistics.services.users.UserService;

@Controller
@RequestMapping("/offices")
public class OfficeController extends BaseController {
    private static final String OFFICE_LIST_VIEW_MODELS = "officeListViewModels";
    private static final String ALL_OFFICES = "all_offices";

    private final OfficeService officeService;
    private final UserService userService;
    private final Mapper mapper;

    @Autowired
    public OfficeController(OfficeService officeService, UserService userService, Mapper mapper) {
        this.officeService = officeService;
        this.userService = userService;
        this.mapper = mapper;
    }

    @GetMapping
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView fetchAll(ModelAndView modelAndView) {
        modelAndView.addObject(OFFICE_LIST_VIEW_MODELS,
                mapper.mapCollection(officeService.getOffices(), OfficeViewModel.class));

        return view(ALL_OFFICES, modelAndView);
    }

    @GetMapping("/manager")
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView addOffice(ModelAndView modelAndView, @ModelAttribute(name = "office") Office office) {
        modelAndView.addObject("office", office);
        return view("add_office");
    }

    @PostMapping
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView postOffice(@ModelAttribute(name = "office") Office office) {
        officeService.createOffice(office);

        return redirect("/offices");
    }

    @GetMapping("/{id}/add-employee")
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView addEmployee(ModelAndView modelAndView, @ModelAttribute(name = "user") UserServiceModel userServiceModel, @PathVariable("id") long id) {
        modelAndView.addObject("user", userServiceModel);
        modelAndView.addObject("id", id);
        return view("add_employee");
    }

    @PostMapping("/{id}/staffing")
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView postEmployee(ModelAndView modelAndView, @PathVariable(name = "id") long id,
            @ModelAttribute(name = "user") UserServiceModel userServiceModel) {

        final UserServiceModel userServiceModelByUsername = userService.findByUsername(userServiceModel.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(UNABLE_TO_FIND_USER_BY_NAME_MESSAGE));

        officeService.addEmployee(id, userServiceModelByUsername);

        modelAndView.setViewName("redirect:" + "/offices");
        return modelAndView;
    }

    @RequestMapping(value="/{id}/delete")
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView deleteOffice(@PathVariable(name = "id") long id ) {
        officeService.removeOffice(id);

        return redirect("/offices");
    }
}
