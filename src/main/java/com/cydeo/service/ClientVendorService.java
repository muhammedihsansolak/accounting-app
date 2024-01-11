package com.cydeo.service;
import com.cydeo.dto.ClientVendorDTO;
import com.cydeo.entity.ClientVendor;
import com.cydeo.enums.ClientVendorType;

import java.util.List;

public interface ClientVendorService {


    ClientVendorDTO findById(Long Id);

    List<ClientVendorDTO> getAllClientVendors();

    ClientVendorDTO saveClientVendor(ClientVendorDTO clientVendorDTO);

    ClientVendorDTO update(Long id , ClientVendorDTO clientVendorDTO);
    void delete (Long Id);

    List<ClientVendorDTO> findByClientVendorType(ClientVendorType clientVendorType);
}
