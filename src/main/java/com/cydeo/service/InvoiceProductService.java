package com.cydeo.service;

import com.cydeo.dto.InvoiceProductDTO;

public interface InvoiceProductService {
    InvoiceProductDTO findById(Long id);
}
