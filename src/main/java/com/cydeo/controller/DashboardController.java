package com.cydeo.controller;

import com.cydeo.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

   private final InvoiceService invoiceService;

    public DashboardController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }


    @GetMapping
    public String openDashboardPage(Model model){

        model.addAttribute("invoices",invoiceService.findTop3ByCompanyOrderByDateDesc());

        return "/dashboard";
    }




}