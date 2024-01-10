package com.cydeo.service;

import com.cydeo.dto.InvoiceDTO;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.enums.InvoiceType;

import java.util.List;

public interface InvoiceService {
    InvoiceDTO findById(Long id);

    List<InvoiceDTO> findAllPurchaseInvoices();

    void update(InvoiceDTO foundInvoice, InvoiceDTO invoiceToUpdate);

    void deleteInvoice(Long invoiceId);

    void approve(Long invoiceId);

    InvoiceDTO invoiceCreator(InvoiceType invoiceType);

    InvoiceDTO create(InvoiceDTO invoice);

}
