package com.example.logistics.web.controllers;

import static com.example.logistics.commons.constants.AuthorizationConstants.IS_AUTHENTICATED;
import static com.example.logistics.commons.constants.paths.IncomePathParamConstants.INCOME_PATH;
import static com.example.logistics.commons.constants.paths.IncomePathParamConstants.IN_RANGE;
import static com.example.logistics.commons.constants.views.IncomeViewConstants.INCOME;
import static com.example.logistics.commons.constants.views.IncomeViewConstants.INCOME_VIEW_MODEL;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.logistics.services.income.IncomeService;

@Controller
@RequestMapping(INCOME_PATH)
public class IncomeController extends BaseController {
    private final IncomeService incomeService;

    @Autowired
    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @GetMapping(IN_RANGE)
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView fetchIncomeInRange(
            @RequestParam(name = "fromDate") @DateTimeFormat(iso = DATE) LocalDate fromDate,
            @RequestParam(name = "toDate") @DateTimeFormat(iso = DATE) LocalDate toDate, ModelAndView modelAndView) {
        modelAndView.addObject(INCOME_VIEW_MODEL, incomeService.getIncomeByTimePeriod(fromDate, toDate));
        return view(INCOME, modelAndView);
    }

    @GetMapping
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView getIncomeView(ModelAndView modelAndView) {
        return view(INCOME);
    }
}
