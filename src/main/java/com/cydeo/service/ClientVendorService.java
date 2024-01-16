package com.cydeo.service;
import com.cydeo.dto.ClientVendorDTO;
import com.cydeo.enums.ClientVendorType;
import org.springframework.validation.BindingResult;


import java.util.List;

public interface ClientVendorService {


    ClientVendorDTO findById(Long Id);
    ClientVendorDTO saveClientVendor(ClientVendorDTO clientVendorDTO);
    ClientVendorDTO update(Long id , ClientVendorDTO clientVendorDTO);
    List<ClientVendorDTO> findByClientVendorType(ClientVendorType clientVendorType);
    List<ClientVendorDTO> getAllClientVendors();
    void delete (Long Id);


    BindingResult addTypeValidation(String type, BindingResult bindingResult);

}
