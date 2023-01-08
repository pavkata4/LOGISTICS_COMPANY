package com.example.logistics.web.controllers;

import static com.example.logistics.commons.constants.AuthorizationConstants.IS_AUTHENTICATED;
import static com.example.logistics.commons.constants.AuthorizationConstants.UNABLE_TO_FIND_USER_BY_NAME_MESSAGE;
import static com.example.logistics.commons.constants.paths.OfficePathParamConstants.MANAGER;
import static com.example.logistics.commons.constants.paths.OfficePathParamConstants.OFFICES;
import static com.example.logistics.commons.constants.views.OfficeViewConstants.ALL_OFFICES;
import static com.example.logistics.commons.constants.views.OfficeViewConstants.OFFICE_LIST_VIEW_MODELS;

import com.example.logistics.domain.models.service.OfficeServiceModel;
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

import com.example.logistics.commons.utils.Mapper;
import com.example.logistics.domain.models.service.UserServiceModel;
import com.example.logistics.domain.models.view.OfficeViewModel;
import com.example.logistics.services.offices.OfficeService;
import com.example.logistics.services.users.UserService;


@Controller
@RequestMapping(OFFICES)
public class OfficeController extends BaseController {
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

    @GetMapping(MANAGER)
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView addOffice(ModelAndView modelAndView, @ModelAttribute(name = "office") OfficeServiceModel officeServiceModel) {
        modelAndView.addObject("office", officeServiceModel);
        return view("add_office");
    }

    @PostMapping
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView postOffice(@ModelAttribute(name = "office") OfficeServiceModel officeServiceModel) {
        officeService.createOffice(officeServiceModel);

        return redirect("/offices");
    }

    @GetMapping("/{id}/update")
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView updateOffice(ModelAndView modelAndView,
            @ModelAttribute(name = "office") OfficeServiceModel officeServiceModel, @PathVariable("id") long id) {
        officeServiceModel = officeService.findOfficeById(id)
                .orElseThrow();

        modelAndView.addObject("office", officeServiceModel);
        return view("edit_office");
    }

    @RequestMapping("/update")
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView putOffice(@ModelAttribute(name = "office") OfficeServiceModel officeServiceModel) {
        officeService.updateOffice(officeServiceModel);

        return redirect("/offices");
    }

    @GetMapping("/{id}/add-employee")
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView addEmployee(ModelAndView modelAndView, @ModelAttribute(name = "user") UserServiceModel userServiceModel, @PathVariable("id") long id) {
        modelAndView.addObject("user", userServiceModel);
        return view("add_employee");
    }

    @GetMapping("/{id}/add-courier")
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView addCourier(ModelAndView modelAndView, @ModelAttribute(name = "user") UserServiceModel userServiceModel, @PathVariable("id") long id) {
        modelAndView.addObject("user", userServiceModel);
        return view("add_courier");
    }

    @GetMapping("/{id}/remove_employee-courier")
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView operationsWithUsers(ModelAndView modelAndView, @ModelAttribute(name = "user") UserServiceModel userServiceModel, @PathVariable("id") long id) {
        modelAndView.addObject("user", userServiceModel);
        return view("remove_employee-courier");
    }

    @PostMapping("/{id}/staffing/{authority}")
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView operateWithEmployeeOrCourier(ModelAndView modelAndView, @PathVariable(name = "id") long id,
                                    @ModelAttribute(name = "user") UserServiceModel userServiceModel, @PathVariable(name = "authority") String authority) {

        final UserServiceModel userServiceModelByUsername = userService.findByUsername(userServiceModel.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(UNABLE_TO_FIND_USER_BY_NAME_MESSAGE));

        officeService.operationsWithUsers(id, userServiceModelByUsername, authority);

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
