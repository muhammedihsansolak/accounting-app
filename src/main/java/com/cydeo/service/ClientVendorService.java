package com.cydeo.service;
import com.cydeo.dto.ClientVendorDTO;
import com.cydeo.entity.Company;
import com.cydeo.enums.ClientVendorType;
import org.springframework.validation.BindingResult;


import java.util.List;

public interface ClientVendorService {


    ClientVendorDTO findById(Long Id);
    ClientVendorDTO saveClientVendor(ClientVendorDTO clientVendorDTO);
    ClientVendorDTO update(Long id , ClientVendorDTO clientVendorDTO);
    List<ClientVendorDTO> getAllClientVendors();
    void delete (Long Id);


    List<ClientVendorDTO> findClientVendorByClientVendorTypeAndCompany(ClientVendorType clientVendorType);

    BindingResult addTypeValidation(String type, BindingResult bindingResult);

}