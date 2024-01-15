package com.cydeo.service.impl;

import com.cydeo.dto.ClientVendorDTO;
import com.cydeo.dto.CompanyDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Company;
import com.cydeo.enums.ClientVendorType;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.entity.ClientVendor;
import com.cydeo.service.ClientVendorService;
import com.cydeo.service.SecurityService;
import org.springframework.stereotype.Service;
import com.cydeo.repository.ClientVendorRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;



@Service
public class ClientVendorServiceImpl implements ClientVendorService {


    private final ClientVendorRepository clientVendorRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;

    public ClientVendorServiceImpl(ClientVendorRepository clientVendorRepository, MapperUtil mapperUtil, SecurityService securityService) {
        this.clientVendorRepository = clientVendorRepository;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
    }


    @Override
    public ClientVendorDTO findById(Long id) {
        ClientVendor clientVendor = clientVendorRepository.findById(id).orElseThrow(()->
                new NoSuchElementException("Client vendor cannot be found with id: "+id));
        return mapperUtil.convert(clientVendor,new ClientVendorDTO());
    }



    @Override
    public List<ClientVendorDTO> getAllClientVendors() {
        UserDTO loggedInUser = securityService.getLoggedInUser();
        List<ClientVendor> clientVendors = clientVendorRepository.findAllByCompanyId(loggedInUser.getCompany().getId());
        return clientVendors.stream().map
                        (clientVendor -> mapperUtil.convert(clientVendor,new ClientVendorDTO()))
                .collect(Collectors.toList());
    }


    @Override
    public ClientVendorDTO saveClientVendor(ClientVendorDTO clientVendorDTO) {
        UserDTO loggedInUser = securityService.getLoggedInUser();
        CompanyDTO companyDTO = loggedInUser.getCompany();
        Company company = mapperUtil.convert(companyDTO, new Company());

        ClientVendor clientVendorToSave = mapperUtil.convert(clientVendorDTO, new ClientVendor());
        clientVendorToSave.setCompany(company);//set company
        ClientVendor savedClientVendor = clientVendorRepository.save(clientVendorToSave);

        return mapperUtil.convert(savedClientVendor, new ClientVendorDTO());
    }

    @Override
    public ClientVendorDTO update(Long id ,ClientVendorDTO clientVendorDTO) {
        ClientVendor byId = clientVendorRepository.findById(id).orElseThrow();
        ClientVendor clientVendor = mapperUtil.convert(clientVendorDTO, new ClientVendor());

        clientVendor.setId(byId.getId());
        clientVendor.setCompany( byId.getCompany() );

        ClientVendor saved = clientVendorRepository.save(clientVendor);
        return mapperUtil.convert(saved, new ClientVendorDTO());
    }

    @Override
    public void delete(Long id) {
        ClientVendor byId = clientVendorRepository.findById(id).orElseThrow();
        byId.setIsDeleted(Boolean.TRUE);
        clientVendorRepository.save(byId);


    }

    @Override
    public List<ClientVendorDTO> findByClientVendorType(ClientVendorType clientVendorType) {
        UserDTO loggedInUser = securityService.getLoggedInUser();
        CompanyDTO companyDTO = loggedInUser.getCompany();
        Company company = mapperUtil.convert(companyDTO, new Company());

        List<ClientVendor> byClientVendorType
                = clientVendorRepository.findByClientVendorTypeAndCompany(clientVendorType, company);
        return  byClientVendorType.stream()
                .map(clientVendor -> mapperUtil.convert(clientVendor,new ClientVendorDTO()))
                .collect(Collectors.toList());

    }
}

