package com.cydeo.service.impl;

import com.cydeo.dto.CompanyDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Company;
import com.cydeo.enums.CompanyStatus;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.CompanyRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Company company = repository.findById(companyId)
                .orElseThrow(() -> new NoSuchElementException("Company with id: " + companyId + " Not Found "));
            return mapperUtil.convert(company, new CompanyDTO());
    }

    @Override
    public List<CompanyDTO> getCompanyList() {
        List<Company> companies = repository.findAll();
        if (companies.size() !=0){
            return companies.stream()
                    .map(company -> mapperUtil.convert(company,new CompanyDTO()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public CompanyDTO updateCompany(CompanyDTO newCompanyDto, Long companyId) {
        Optional<Company> oldCompany = repository.findById(companyId);
        if (oldCompany.isPresent()) {
            CompanyStatus oldCompanyStatus = oldCompany.get().getCompanyStatus();
            newCompanyDto.setCompanyStatus(oldCompanyStatus);

            Company savedCompany = repository.save(mapperUtil.convert(newCompanyDto,new Company()));

            return mapperUtil.convert(savedCompany,newCompanyDto);
        }
        return null;
    }

    @Override
    public CompanyDTO createCompany(CompanyDTO newCompany) {
        newCompany.setCompanyStatus(CompanyStatus.PASSIVE);
        Company savedCompany = repository.save(mapperUtil.convert(newCompany,new Company()));

        return mapperUtil.convert(savedCompany, new CompanyDTO());
    }

    @Override
    public void activateCompany(long companyId) {
        Company companyToBeActivate = repository.findById(companyId)
                .orElseThrow(() -> new NoSuchElementException("Company with id: " + companyId + " Not Found "));
        companyToBeActivate.setCompanyStatus(CompanyStatus.ACTIVE);
        repository.save(companyToBeActivate);

    }

    @Override
    public void deactivateCompany(long companyId) {
        Company companyToBeDeactivate = repository.findById(companyId)
                .orElseThrow(() -> new NoSuchElementException("Company with id: " + companyId + " Not Found "));
        companyToBeDeactivate.setCompanyStatus(CompanyStatus.PASSIVE);
        repository.save(companyToBeDeactivate);
    }
}
