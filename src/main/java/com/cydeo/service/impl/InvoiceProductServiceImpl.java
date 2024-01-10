package com.cydeo.service.impl;

import com.cydeo.dto.InvoiceProductDTO;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.InvoiceProductRepository;
import com.cydeo.service.InvoiceProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final InvoiceProductRepository repository;
    private final MapperUtil mapper;

    @Override
    public InvoiceProductDTO findById(Long id) {
        InvoiceProduct foundInvoiceProduct = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("InvoiceProduct can not found with id: " + id));

        return mapper.convert(foundInvoiceProduct, new InvoiceProductDTO());
    }
}
