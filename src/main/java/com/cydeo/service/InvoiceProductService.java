package com.cydeo.service;

import com.cydeo.dto.InvoiceDTO;
import com.cydeo.dto.InvoiceProductDTO;
import com.cydeo.enums.InvoiceStatus;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface InvoiceProductService {
    InvoiceProductDTO findById(Long id);
    List<InvoiceProductDTO> findByInvoiceId(Long invoiceId);
    InvoiceProductDTO deleteById(Long id);
    void removeInvoiceProductFromInvoice(Long invoiceId, Long invoiceProductId);

    InvoiceProductDTO create(InvoiceProductDTO invoiceProductDTO, Long invoiceId);

    BindingResult doesProductHaveEnoughStock(InvoiceProductDTO invoiceProductDTO, BindingResult bindingResult );

    boolean doesProductHasInvoice(Long productId);

    void deleteByInvoice(InvoiceDTO invoice);
    List<InvoiceProductDTO> findAllApprovedInvoiceInvoiceProduct(InvoiceStatus invoiceStatus);
}
