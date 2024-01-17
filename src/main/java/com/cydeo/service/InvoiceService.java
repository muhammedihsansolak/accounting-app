package com.cydeo.service;

import com.cydeo.dto.InvoiceDTO;
import com.cydeo.dto.InvoiceProductDTO;
import com.cydeo.enums.InvoiceType;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceService {
    InvoiceDTO findById(Long id);

    List<InvoiceDTO> findAllInvoices(InvoiceType invoiceType);

    void update(InvoiceDTO foundInvoice, InvoiceDTO invoiceToUpdate);

    void deleteInvoice(Long invoiceId);

    void approve(Long invoiceId);

    InvoiceDTO invoiceGenerator(InvoiceType invoiceType);

    InvoiceDTO create(InvoiceDTO invoice, InvoiceType invoiceType);

    BigDecimal calculateTaxForProduct(InvoiceProductDTO invoiceProductDTO);
}
