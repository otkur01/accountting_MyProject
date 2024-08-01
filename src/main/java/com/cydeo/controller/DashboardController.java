package com.cydeo.controller;


import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.UserDto;
import com.cydeo.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;


    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public String getUserToDashboard(Model model){

        Map<String,BigDecimal> summaryNumbers = new HashMap<>();
        summaryNumbers.put("totalCost", dashboardService.getTotalCost());
        summaryNumbers.put("totalSales", dashboardService.getTotalSales());
        summaryNumbers.put("profitLoss", dashboardService.getProfitLoss());
        List<InvoiceDto> invoices= dashboardService.last3ApprovedInvoiceByCompany();

        model.addAttribute("summaryNumbers", summaryNumbers);
        model.addAttribute("invoices", invoices);
        model.addAttribute("exchangeRates", dashboardService.exchangeForUsd());

        return "dashboard";

    }
}
