package com.cydeo.service.impl;

import com.cydeo.dto.CompanyDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Company;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.CompanyRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final UserService userService;
    private final CompanyRepository repository;
    private final MapperUtil mapperUtil;

    @Override
    public CompanyDTO getCompanyDtoByLoggedInUser() {
        String loggedInUser_userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (loggedInUser_userName != null){
           UserDTO loggedInUserDto = userService.findByUsername(loggedInUser_userName);
           return loggedInUserDto.getCompany();
        }
        return null;
    }

    @Override
    public CompanyDTO findById(Long companyId) {
        Optional<Company> company = repository.findById(companyId);
        if (company.isPresent()) {
            return mapperUtil.convert(company, new CompanyDTO());
        }
        return null;
    }
}
