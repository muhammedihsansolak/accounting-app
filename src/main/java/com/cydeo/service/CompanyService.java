package com.cydeo.service;

import com.cydeo.dto.CompanyDTO;

public interface CompanyService {
    CompanyDTO getCompanyDtoByLoggedInUser();

    CompanyDTO findById(Long companyId);
}
