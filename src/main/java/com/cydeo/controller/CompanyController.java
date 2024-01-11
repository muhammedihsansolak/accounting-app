package com.cydeo.controller;

import com.cydeo.dto.CompanyDTO;
import com.cydeo.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/update/{companyId}")
    public String updateCompanies(@PathVariable("companyId") Long companyId, Model model){

        model.addAttribute("company", companyService.getCompanyById(companyId));

        return "/company/company-update";
    }

    @PostMapping("/update/{companyId}")
    public String updateCompanies(@PathVariable("companyId") Long companyId,
                                  @ModelAttribute("company")CompanyDTO company){
        companyService.updateCompany(company,companyId);
        return "redirect:/companies/list";
    }

}
