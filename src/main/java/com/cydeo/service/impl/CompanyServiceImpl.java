package com.cydeo.service.impl;

import com.cydeo.dto.CompanyDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Company;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.CompanyRepository;
import com.cydeo.enums.CompanyStatus;

import com.cydeo.service.CompanyService;
import com.cydeo.service.SecurityService;
import com.cydeo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final SecurityService securityService;
    private final MapperUtil mapperUtil;
    private final CompanyRepository repository;


    @Override
    public CompanyDTO findById(Long companyId) {
        if (companyId !=1) {
            Company company = repository.findById(companyId)
                    .orElseThrow(() -> new NoSuchElementException("Company with id: " + companyId + " Not Found "));
            return mapperUtil.convert(company, new CompanyDTO());
        }
        return null;
    }

    @Override
    public List<CompanyDTO> getCompanyList() {
        List<Company> companies = repository.findAllCompanyIdNot1();
        if (companies.size() !=0){
            return companies.stream()
                    .map(company -> mapperUtil.convert(company,new CompanyDTO()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<CompanyDTO> getCompanyDtoByLoggedInUser() {
        UserDTO loggedInUser = securityService.getLoggedInUser();
        if (loggedInUser.getRole().getId() == 1) {
            List<Company> companies = repository.getAllCompanyForRoot(loggedInUser.getCompany().getId());
            return companies.stream().map(company -> mapperUtil.convert(company, new CompanyDTO()))
                    .collect(Collectors.toList());
        } else {
            Company company = repository.getCompanyForCurrent(loggedInUser.getCompany().getId());
            return Collections.singletonList(mapperUtil.convert(company, new CompanyDTO()));
        }
    }

        public CompanyDTO updateCompany (CompanyDTO newCompanyDto){
            Optional<Company> oldCompany = repository.findById(newCompanyDto.getId());
            if (oldCompany.isPresent()) {
                CompanyStatus oldCompanyStatus = oldCompany.get().getCompanyStatus();
                newCompanyDto.setCompanyStatus(oldCompanyStatus);
                Company savedCompany = repository.save(mapperUtil.convert(newCompanyDto, new Company()));

                return mapperUtil.convert(savedCompany, newCompanyDto);
            }
            return null;

        }

        @Override
        public CompanyDTO createCompany (CompanyDTO newCompany){
            newCompany.setCompanyStatus(CompanyStatus.PASSIVE);
            Company savedCompany = repository.save(mapperUtil.convert(newCompany, new Company()));

            return mapperUtil.convert(savedCompany, new CompanyDTO());
        }

        @Override
        public void activateCompany ( long companyId){
            Company companyToBeActivate = repository.findById(companyId)
                    .orElseThrow(() -> new NoSuchElementException("Company with id: " + companyId + " Not Found "));
            companyToBeActivate.setCompanyStatus(CompanyStatus.ACTIVE);
            repository.save(companyToBeActivate);

        }

        @Override
        public void deactivateCompany ( long companyId){
            Company companyToBeDeactivate = repository.findById(companyId)
                    .orElseThrow(() -> new NoSuchElementException("Company with id: " + companyId + " Not Found "));
            companyToBeDeactivate.setCompanyStatus(CompanyStatus.PASSIVE);
            repository.save(companyToBeDeactivate);
        }

    @Override
    public BindingResult addTitleValidation(String title, BindingResult bindingResult) {
        if (repository.existsByTitle(title)){
            bindingResult.addError(new FieldError("newCompany", "title", "This title already exists."));
        }
        return bindingResult;
    }
}