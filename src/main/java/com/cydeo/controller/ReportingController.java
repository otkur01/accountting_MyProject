package com.cydeo.controller;

import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.ReportingService;
import com.cydeo.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reports")
public class ReportingController {


    private final ReportingService reportingService;



    private final SecurityService securityService;


    @Autowired
    public ReportingController( ReportingService reportingService, SecurityService securityService) {

        this.reportingService = reportingService;
        this.securityService = securityService;
    }

    @GetMapping("/profitLossData")
    private String profitLoss(Model model){
        model.addAttribute("monthlyProfitLossDataMap", reportingService.MonthlyProfitLossData());
        return  "/report/profit-loss-report";
    }



    @GetMapping("/stockData")
    public String getStockData(Model model){
        model.addAttribute("invoiceProducts",reportingService.getAllInvoiceProductByCompanyId());


        return "/report/stock-report";
    }



}
