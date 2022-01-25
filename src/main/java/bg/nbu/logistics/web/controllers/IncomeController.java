package bg.nbu.logistics.web.controllers;

import static bg.nbu.logistics.commons.constants.AuthorizationConstants.IS_AUTHENTICATED;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import bg.nbu.logistics.services.income.IncomeService;

@Controller
@RequestMapping("income")
public class IncomeController extends BaseController {
    private final IncomeService incomeService;

    @Autowired
    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @GetMapping
    @PreAuthorize(IS_AUTHENTICATED)
    public ModelAndView fetchIncome(ModelAndView modelAndView) {
        final double income = incomeService.getIncomeByTimePeriod(LocalDate.now()
                .minusMonths(2),
                LocalDate.now()
                        .plusMonths(3));

        modelAndView.addObject("income", income);
        return view("income", modelAndView);
    }
}
