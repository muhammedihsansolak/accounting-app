package com.cydeo.service;

import com.cydeo.dto.CompanyDTO;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface CompanyService {
    List<CompanyDTO> getCompanyDtoByLoggedInUser();

    CompanyDTO findById(Long id);

}
