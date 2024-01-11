package com.cydeo.service;

import com.cydeo.dto.CompanyDTO;

import java.util.List;

public interface CompanyService {
    CompanyDTO getCompanyDtoByLoggedInUser();

    CompanyDTO findById(Long companyId);

    List<CompanyDTO> getCompanyList();

    CompanyDTO getCompanyById(Long companyId);
}
