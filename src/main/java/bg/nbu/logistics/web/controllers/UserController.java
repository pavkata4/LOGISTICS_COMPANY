package bg.nbu.logistics.web.controllers;

import static bg.nbu.logistics.commons.constants.AuthorizationConstants.IS_ANONYMOUS;
import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import bg.nbu.logistics.domain.models.binding.UserRegisterBindingModel;
import bg.nbu.logistics.domain.models.service.UserServiceModel;
import bg.nbu.logistics.domain.models.view.UserViewModel;
import bg.nbu.logistics.services.users.UserService;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {
    private static final String USER_REGISTER_BINDING_MODEL = "userRegisterBindingModel";
    public static final String REGISTER_PATH = "/register";
    public static final String REGISTRATION = "registration";
    public static final String ALL_USERS = "users/all_users";
    public static final String USERS = "/users";
    public static final String LOGIN_PATH = "/login";
    public static final String LOGIN = "login";
    public static final String DELETE = "/delete";

    private final ModelMapper modelMapper;
    private final UserService userService;

    @Autowired
    public UserController(ModelMapper modelMapper, UserService userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
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

        return redirect(USERS + LOGIN_PATH);
    }

    @GetMapping(LOGIN_PATH)
    @PreAuthorize(IS_ANONYMOUS)
    public ModelAndView login() {
        return view(LOGIN);
    }
    
    @DeleteMapping(DELETE + "/{id}")
    public ModelAndView delete(@PathVariable(name = "id") long id) {
        userService.delete(id);

        return redirect(USERS);
    }

    @GetMapping
    public ModelAndView fetchAll(ModelAndView modelAndView) {
        final List<UserViewModel> userViewModels = userService.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserViewModel.class))
                .collect(toUnmodifiableList());
        modelAndView.addObject("userListViewModels", userViewModels);

        return view(ALL_USERS, modelAndView);
    }
}