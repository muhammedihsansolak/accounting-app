package com.cydeo.controller;

import com.cydeo.dto.CompanyDTO;
import com.cydeo.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("/create")
    public String createCompany(Model model){
        model.addAttribute("newCompany", new CompanyDTO());
        model.addAttribute("countries",companyService.getCounties());
//        model.addAttribute("countries", List.of("USA","UK")); // we will consume from third party app

        return "/company/company-create";
    }

    @PostMapping("/create")
    public String createCompany(@Valid @ModelAttribute("newCompany") CompanyDTO newCompany,
                                BindingResult bindingResult, Model model){

        // Title cannot be null and should be unique
        bindingResult = companyService.addTitleValidation(newCompany.getTitle(),bindingResult);

        if (bindingResult.hasFieldErrors()){
            model.addAttribute("countries",companyService.getCounties());
//            model.addAttribute("countries", List.of("USA","UK")); // we will consume from third party app
            return "/company/company-create";
        }

        companyService.createCompany(newCompany);

        return "redirect:/company/list";
    }

    @GetMapping("/update/{companyId}")
    public String updateCompanies(@PathVariable("companyId") Long companyId, Model model){

        model.addAttribute("company", companyService.findById(companyId));
        model.addAttribute("countries",companyService.getCounties());
//        model.addAttribute("countries", List.of("USA","UK")); // we will consume from third party app

        return "/company/company-update";
    }

    @PostMapping("/update/{id}")
    public String updateCompanies(@Valid @ModelAttribute("company")CompanyDTO company,
                                  BindingResult bindingResult, Model model){
        // Title cannot be null and should be unique
        bindingResult = companyService.addUpdateTitleValidation(company,bindingResult);

        if (bindingResult.hasFieldErrors()){
            model.addAttribute("countries",companyService.getCounties());
//            model.addAttribute("countries", List.of("USA","UK")); // we will consume from third party app
            return "/company/company-update";
        }

        companyService.updateCompany(company);
        return "redirect:/companies/list";
    }
    @GetMapping("/activate/{company_id}")
    public String activateCompany(@PathVariable("company_id") long company_id, Model model){
        companyService.activateCompany(company_id);
        System.out.println("hello: " + company_id);
        return "redirect:/companies/list";
    }

    @GetMapping("/deactivate/{company_id}")
    public String deactivateCompany(@PathVariable("company_id") long company_id, Model model){
        companyService.deactivateCompany(company_id);
        return "redirect:/companies/list";
    }

}
