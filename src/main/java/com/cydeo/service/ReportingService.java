package com.cydeo.service;

import com.cydeo.dto.InvoiceProductDTO;

import java.util.List;

public interface ReportingService {
    List<InvoiceProductDTO> getInvoiceProductList();
}
