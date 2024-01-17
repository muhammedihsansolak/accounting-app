package com.cydeo.controller;

import com.cydeo.service.DashboardService;
import com.cydeo.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

   private final InvoiceService invoiceService;
   private final DashboardService dashboardService;

    public DashboardController(InvoiceService invoiceService, DashboardService dashboardService) {
        this.invoiceService = invoiceService;
        this.dashboardService = dashboardService;
    }


    @GetMapping
    public String openDashboardPage(Model model){

        model.addAttribute("invoices",invoiceService.findTop3ByCompanyOrderByDateDesc());
        model.addAttribute("summaryNumbers",dashboardService.totalCost());
        model.addAttribute("summaryNumbers",dashboardService.totalSales());
        model.addAttribute("summaryNumbers",dashboardService.profitLoss());

        return "/dashboard";
    }




}