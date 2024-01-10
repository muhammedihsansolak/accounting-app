package com.cydeo.service;
import com.cydeo.dto.ClientVendorDTO;
import com.cydeo.entity.ClientVendor;

import java.util.List;

public interface ClientVendorService {

    List<ClientVendor> getAllClientVendors();
    ClientVendor getClientVendorById(Long id);
    ClientVendor saveClientVendor(ClientVendor clientVendor);
    void deleteClientVendor(Long id);
}
