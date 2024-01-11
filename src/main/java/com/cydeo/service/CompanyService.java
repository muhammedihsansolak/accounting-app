package com.cydeo.service;

import com.cydeo.dto.CompanyDTO;

import java.util.List;

public interface CompanyService {
    CompanyDTO getCompanyDtoByLoggedInUser();

    CompanyDTO findById(Long companyId);

    List<CompanyDTO> getCompanyList();

    CompanyDTO updateCompany(CompanyDTO company);

    CompanyDTO createCompany(CompanyDTO newCompany);

    void activateCompany(long companyId);

    void deactivateCompany(long companyId);
}
