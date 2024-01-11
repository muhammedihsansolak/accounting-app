package com.cydeo.controller;

import com.cydeo.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/companies")
public class CompanyController {
    private final CompanyService companyService;
    @GetMapping("/list")
    public String getCompanyList(Model model){

        model.addAttribute("companies", companyService.getCompanyList());

        return "/company/company-list";
    }
}
