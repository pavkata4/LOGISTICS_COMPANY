package com.example.logistics.web.controllers;

import static com.example.logistics.commons.constants.AuthorizationConstants.IS_ANONYMOUS;
import static com.example.logistics.commons.constants.AuthorizationConstants.IS_AUTHENTICATED;
import static com.example.logistics.commons.constants.paths.UserPathParamConstants.LOGIN_PATH;
import static com.example.logistics.commons.constants.paths.UserPathParamConstants.REGISTER_PATH;
import static com.example.logistics.commons.constants.paths.UserPathParamConstants.USERS;
import static com.example.logistics.commons.constants.views.UserViewConstants.ALL_USERS;
import static com.example.logistics.commons.constants.views.UserViewConstants.EMPLOYEE_LIST_VIEW_MODELS;
import static com.example.logistics.commons.constants.views.UserViewConstants.LOGIN;
import static com.example.logistics.commons.constants.views.UserViewConstants.REGISTRATION;
import static com.example.logistics.commons.constants.views.UserViewConstants.USER_LIST_VIEW_MODELS;
import static com.example.logistics.commons.constants.views.UserViewConstants.USER_REGISTER_BINDING_MODEL;
import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriBuilder;

import com.example.logistics.domain.models.binding.UserRegisterBindingModel;
import com.example.logistics.domain.models.service.UserServiceModel;
import com.example.logistics.domain.models.view.UserViewModel;
import com.example.logistics.services.users.UserService;

@Controller
@RequestMapping(USERS)
public class UserController extends BaseController {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final UriBuilder uriBuilder;

    @Autowired
    public UserController(ModelMapper modelMapper, UserService userService, UriBuilder uriBuilder) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.uriBuilder = uriBuilder;
    }

    @GetMapping(REGISTER_PATH)
    @PreAuthorize(IS_ANONYMOUS)
    public ModelAndView register(ModelAndView modelAndView,
            @ModelAttribute(name = USER_REGISTER_BINDING_MODEL) UserRegisterBindingModel userRegisterBindingModel) {
        modelAndView.addObject(USER_REGISTER_BINDING_MODEL, userRegisterBindingModel);

        return view(REGISTRATION, modelAndView);
    }

    @PostMapping(REGISTER_PATH)
    @PreAuthorize(IS_ANONYMOUS)
    public ModelAndView registerConfirm(
            @Valid @ModelAttribute(name = USER_REGISTER_BINDING_MODEL) UserRegisterBindingModel userRegisterBindingModel) {
        userService.register(modelMapper.map(userRegisterBindingModel, UserServiceModel.class));

        return redirect(uriBuilder.pathSegment(USERS, LOGIN_PATH)
                .build()
                .toString());
    }

    @GetMapping(LOGIN_PATH)
    @PreAuthorize(IS_ANONYMOUS)
    public ModelAndView login() {
        return view(LOGIN);
    }
    
    @GetMapping("/{id}/delete")
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView delete(@PathVariable(name = "id") long id) {
        userService.delete(id);

        return redirect(USERS);
    }

    @GetMapping
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView fetchUsers(ModelAndView modelAndView) {
        final List<UserViewModel> userViewModels = userService.findAllUsers()
                .stream()
                .map(user -> modelMapper.map(user, UserViewModel.class))
                .collect(toUnmodifiableList());
        final List<UserViewModel> employeeViewModels = userService.findAllEmployees()
                .stream()
                .map(user -> modelMapper.map(user, UserViewModel.class))
                .collect(toUnmodifiableList());

        modelAndView.addObject(USER_LIST_VIEW_MODELS, userViewModels);
        modelAndView.addObject(EMPLOYEE_LIST_VIEW_MODELS, employeeViewModels);

        return view(ALL_USERS, modelAndView);
    }

    @RequestMapping
    @PreAuthorize(IS_AUTHENTICATED)
    public void deleteUser(@PathVariable("id") long id) {
        userService.delete(id);
    }

    @GetMapping("/{id}/update")
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView updateUser(ModelAndView modelAndView, @ModelAttribute("user") UserServiceModel userServiceModel, @PathVariable("id") long id) {
        userServiceModel = userService.findById(id);

        modelAndView.addObject("user", userServiceModel);
        return view("edit_user");
    }

    @RequestMapping("/update")
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView putUser(@ModelAttribute(name = "user") UserServiceModel userServiceModel) {
        userService.update(userServiceModel);

        return redirect("/users");
    }
}