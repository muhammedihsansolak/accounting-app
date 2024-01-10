package com.cydeo.service.impl;

import com.cydeo.entity.ClientVendor;
import com.cydeo.service.ClientVendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cydeo.repository.ClientVendorRepository;

import java.util.List;


@Service
    public class ClientVendorServiceImpl implements ClientVendorService {

    @Autowired
    private ClientVendorRepository clientVendorRepository;

    @Override
    public List<ClientVendor> getAllClientVendors() {
        return clientVendorRepository.findAll();
    }

    @Override
    public ClientVendor getClientVendorById(Long id) {
        return clientVendorRepository.findById(id).orElse(null);
    }

    @Override
    public ClientVendor saveClientVendor(ClientVendor clientVendor) {
        return clientVendorRepository.save(clientVendor);
    }

    @Override
    public void deleteClientVendor(Long id) {
        clientVendorRepository.deleteById(id);
    }
    }

