package bg.nbu.logistics.web.controllers;

import static bg.nbu.logistics.commons.constants.AuthorizationConstants.IS_AUTHENTICATED;
import static bg.nbu.logistics.commons.constants.AuthorizationConstants.UNABLE_TO_FIND_USER_BY_NAME_MESSAGE;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import bg.nbu.logistics.domain.models.service.UserServiceModel;
import bg.nbu.logistics.services.offices.OfficeService;
import bg.nbu.logistics.services.users.UserService;

@Controller
@RequestMapping("/offices")
public class OfficeController extends BaseController {
    private final OfficeService officeService;
    private final UserService userService;

    public OfficeController(OfficeService officeService, UserService userService) {
        this.officeService = officeService;
        this.userService = userService;
    }

    @PostMapping("/{id}/staffing/{employeeName}")
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView addEmployee(@PathVariable(name = "id") long id,
            @PathVariable(name = "employeeName") String employeeName) {

        final UserServiceModel userServiceModel = userService.findByUsername(employeeName)
                .orElseThrow(() -> new UsernameNotFoundException(UNABLE_TO_FIND_USER_BY_NAME_MESSAGE));

        officeService.addEmployee(id, userServiceModel);

        return view("TO BE IMPLEMENTED");
    }
}
