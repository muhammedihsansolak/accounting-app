package com.cydeo.controller;

import com.cydeo.client.ExchangeClient;
import com.cydeo.dto.InvoiceDTO;
import com.cydeo.service.DashboardService;
import com.cydeo.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

   private final InvoiceService invoiceService;
   private final DashboardService dashboardService;
   private final ExchangeClient exchangeClient;


    public DashboardController(InvoiceService invoiceService, DashboardService dashboardService, ExchangeClient exchangeClient) {
        this.invoiceService = invoiceService;
        this.dashboardService = dashboardService;
        this.exchangeClient = exchangeClient;
    }


    @GetMapping
    public String openDashboardPage(Model model){


        model.addAttribute("invoices",invoiceService.findTop3ByCompanyOrderByDateDesc());
        model.addAttribute("summaryNumbers",dashboardService.summaryNumbersCalculation() );
        model.addAttribute("exchangeRates",exchangeClient.getExchangesRates().getUsd());

        return "/dashboard";
    }




}