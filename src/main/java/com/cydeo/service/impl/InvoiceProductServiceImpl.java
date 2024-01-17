package com.cydeo.service.impl;

import com.cydeo.dto.InvoiceDTO;
import com.cydeo.dto.InvoiceProductDTO;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.InvoiceProductRepository;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final InvoiceProductRepository repository;
    private final MapperUtil mapper;
    private final InvoiceService invoiceService;

    public InvoiceProductServiceImpl(InvoiceProductRepository repository, MapperUtil mapper, @Lazy InvoiceService invoiceService) {
        this.repository = repository;
        this.mapper = mapper;
        this.invoiceService = invoiceService;
    }

    @Override
    public InvoiceProductDTO findById(Long id) {
        InvoiceProduct foundInvoiceProduct = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("InvoiceProduct can not found with id: " + id));

        return mapper.convert(foundInvoiceProduct, new InvoiceProductDTO());
    }

    @Override
    public List<InvoiceProductDTO> findByInvoiceId(Long invoiceId) {
        List<InvoiceProduct> invoiceProductList = repository.findByInvoiceId(invoiceId);

        List<InvoiceProductDTO> invoiceProductDTOList = invoiceProductList.stream()
                .map(invoiceProduct -> mapper.convert(invoiceProduct, new InvoiceProductDTO()))
                .map(invoiceProduct -> {
                    BigDecimal taxAmount = invoiceService.calculateTaxForProduct(invoiceProduct);
                    invoiceProduct.setTotal(
                            invoiceProduct.getPrice()
                                    .multiply(BigDecimal.valueOf(invoiceProduct.getQuantity()))
                                    .add(taxAmount));

                    return invoiceProduct;
                })
                .collect(Collectors.toList());

        return invoiceProductDTOList;
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

    @Override
    public InvoiceProductDTO create(InvoiceProductDTO invoiceProductDTO, Long invoiceId) {
        InvoiceDTO invoiceDTO = invoiceService.findById(invoiceId);

        invoiceProductDTO.setInvoice(invoiceDTO);

        InvoiceProduct invoiceProduct = mapper.convert(invoiceProductDTO, new InvoiceProduct());
        invoiceProduct.setId(null);//bug fix
        InvoiceProduct savedInvoice = repository.save(invoiceProduct);

        return mapper.convert(savedInvoice, new InvoiceProductDTO());
    }

    @Override
    public boolean doesProductHaveEnoughStock(InvoiceProductDTO invoiceProductDTO) {
        if (invoiceProductDTO.getProduct() == null) return false;
        Integer invoiceProductQuantity = invoiceProductDTO.getQuantity();
        Integer quantityInStock = invoiceProductDTO.getProduct().getQuantityInStock();

        return quantityInStock >= invoiceProductQuantity;//if enough stock available, return true
    }
}
