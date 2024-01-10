package com.cydeo.service.impl;

import com.cydeo.dto.InvoiceDTO;
import com.cydeo.dto.InvoiceProductDTO;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.InvoiceProductRepository;
import com.cydeo.service.InvoiceProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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

    @Override
    public List<InvoiceProductDTO> findByInvoiceId(Long invoiceId) {
        List<InvoiceProduct> invoiceProductList = repository.findByInvoiceId(invoiceId);

        return invoiceProductList.stream()
                .map(product -> mapper.convert(product, new InvoiceProductDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public InvoiceDTO deleteById(Long id) {
        InvoiceProduct invoiceToDelete = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("InvoiceProduct can not found with id: " + id));

        invoiceToDelete.setIsDeleted( Boolean.TRUE );

        InvoiceProduct deleted = repository.save(invoiceToDelete);
        return mapper.convert(deleted, new InvoiceDTO());
    }

    @Override
    public void removeInvoiceProductFromInvoice(Long invoiceId, Long invoiceProductId) {
        List<InvoiceProductDTO> invoiceProductDTOList = findByInvoiceId(invoiceId);

        invoiceProductDTOList.stream()
                .filter(invoiceProductDTO -> invoiceProductDTO.getId() == invoiceProductId)
                .forEach(invoiceProductDTO -> deleteById(invoiceProductDTO.getId())); //soft delete
    }
}
