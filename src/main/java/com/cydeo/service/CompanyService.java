package com.cydeo.service;

import com.cydeo.dto.CompanyDTO;
import org.springframework.validation.BindingResult;

import java.util.List;

import java.util.List;

public interface CompanyService {
    List<CompanyDTO> getCompanyDtoByLoggedInUser();

    CompanyDTO findById(Long companyId);

    List<CompanyDTO> getCompanyList();

    CompanyDTO updateCompany(CompanyDTO company);

    CompanyDTO createCompany(CompanyDTO newCompany);

    void activateCompany(long companyId);

    void deactivateCompany(long companyId);
    BindingResult addTitleValidation(String title, BindingResult bindingResult);

    BindingResult addUpdateTitleValidation(CompanyDTO company, BindingResult bindingResult);

    CompanyDTO findByCompanyTitle(String companyTitle);

    List<String> getCounties();

}
