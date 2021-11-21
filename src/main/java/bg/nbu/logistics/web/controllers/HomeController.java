package bg.nbu.logistics.web.controllers;


import static bg.nbu.logistics.commons.constants.AuthorizationConstants.IS_ANONYMOUS;
import static bg.nbu.logistics.commons.constants.AuthorizationConstants.IS_AUTHENTICATED;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class HomeController extends BaseController {
    public static final String INDEX_PATH = "/";
    public static final String HOME_PATH = "/home";
    
    public static final String HOME = "home";
    public static final String INDEX = "index";
    
    @GetMapping(INDEX_PATH)
    @PreAuthorize(IS_ANONYMOUS)
    public ModelAndView index() {
        return view(INDEX);
    }

    @GetMapping(HOME_PATH)
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView home() {
        return view(HOME);
    }
}
