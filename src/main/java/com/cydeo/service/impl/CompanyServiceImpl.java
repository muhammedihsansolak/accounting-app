package com.cydeo.service.impl;

import com.cydeo.dto.CompanyDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Company;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.CompanyRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.SecurityService;
import com.cydeo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final UserService userService;
    private final SecurityService securityService;
    private final CompanyRepository companyRepository;
    private final MapperUtil mapperUtil;

    @Override
    public List<CompanyDTO> getCompanyDtoByLoggedInUser() {
        UserDTO loggedInUser=securityService.getLoggedInUser();
        if (loggedInUser.getRole().getId()==1) {
            List<Company> companies = companyRepository.getAllCompanyForRoot(loggedInUser.getCompany().getId());
            return companies.stream().map(company -> mapperUtil.convert(company, new CompanyDTO()))
                    .collect(Collectors.toList());
        }else{
            Company company= companyRepository.getCompanyForCurrent(loggedInUser.getCompany().getId());
            return Collections.singletonList(mapperUtil.convert(company, new CompanyDTO()));
        }
    }

    @Override
    public CompanyDTO findById(Long id) {
        Company company = companyRepository.findById(id).orElseThrow();
        return mapperUtil.convert(company, new CompanyDTO());
    }

}