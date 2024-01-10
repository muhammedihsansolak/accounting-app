package com.cydeo.service.impl;

import com.cydeo.dto.ClientVendorDTO;
import com.cydeo.enums.ClientVendorType;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.entity.ClientVendor;
import com.cydeo.service.ClientVendorService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import com.cydeo.repository.ClientVendorRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Getter
@Setter
@Service
public class ClientVendorServiceImpl implements ClientVendorService {


    private final ClientVendorRepository clientVendorRepository;
    private final MapperUtil mapperUtil;

    public ClientVendorServiceImpl(ClientVendorRepository clientVendorRepository, MapperUtil mapperUtil) {
        this.clientVendorRepository = clientVendorRepository;
        this.mapperUtil = mapperUtil;
    }


    @Override
    public ClientVendorDTO getClientVendorById(Long id) {
        return clientVendorRepository.findById(id).
                map(clientVendor -> mapperUtil.convert(clientVendor,new ClientVendorDTO()))

                .orElse(null);
    }



    @Override
    public List<ClientVendorDTO> getAllClientVendors() {
        List<ClientVendor> clientVendors = clientVendorRepository.findAll();
        return clientVendors.stream().map
       (clientVendor -> mapperUtil.convert(clientVendor,new ClientVendorDTO()))
                .collect(Collectors.toList());
    }


    @Override
    public ClientVendorDTO saveClientVendor(ClientVendorDTO clientVendorDTO) {
        ClientVendor clientVendor = mapperUtil.convert(clientVendorDTO, new ClientVendor());
        ClientVendor savedClientVendor = clientVendorRepository.save(clientVendor);
        return mapperUtil.convert(savedClientVendor, new ClientVendorDTO());
    }

    @Override
    public ClientVendorDTO update(Long id ,ClientVendorDTO clientVendorDTO) {
        ClientVendor byId = clientVendorRepository.findById(id).orElseThrow();
        clientVendorDTO.setId(byId.getId());
        ClientVendor save = clientVendorRepository.save(mapperUtil.convert(clientVendorDTO, new ClientVendor()));
        return mapperUtil.convert(save, new ClientVendorDTO());
    }

    @Override
    public void delete(Long id) {
        ClientVendor byId = clientVendorRepository.findById(id).orElseThrow();
        byId.setIsDeleted(Boolean.TRUE);
        clientVendorRepository.save(byId);


    }


    @Override
    public List<ClientVendorDTO> findByClientVendorType(ClientVendorType clientVendorType) {
        List<ClientVendor> byClientVendorType
                = clientVendorRepository.findByClientVendorType(clientVendorType);
        return  byClientVendorType.stream()
                .map(clientVendor -> mapperUtil.convert(clientVendor,new ClientVendorDTO()))
                .collect(Collectors.toList());

    }
}

