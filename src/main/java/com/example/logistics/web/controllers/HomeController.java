package com.example.logistics.web.controllers;

import static com.example.logistics.commons.constants.AuthorizationConstants.IS_ANONYMOUS;
import static com.example.logistics.commons.constants.AuthorizationConstants.IS_AUTHENTICATED;
import static com.example.logistics.commons.constants.paths.PathParamConstants.HOME;
import static com.example.logistics.commons.constants.paths.PathParamConstants.HOME_PATH;
import static com.example.logistics.commons.constants.paths.PathParamConstants.INDEX;
import static com.example.logistics.commons.constants.paths.PathParamConstants.INDEX_PATH;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController extends BaseController {
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
