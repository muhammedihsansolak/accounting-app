package com.cydeo.service;

import com.cydeo.dto.InvoiceDTO;
import com.cydeo.dto.InvoiceProductDTO;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceProductService {
    InvoiceProductDTO findById(Long id);
    List<InvoiceProductDTO> findByInvoiceId(Long invoiceId);
    InvoiceDTO deleteById(Long id);
    void removeInvoiceProductFromInvoice(Long invoiceId, Long invoiceProductId);

    InvoiceProductDTO create(InvoiceProductDTO invoiceProductDTO, Long invoiceId);

    boolean doesProductHaveEnoughStock(InvoiceProductDTO invoiceProductDTO);
}
