package com.cydeo.service;

import com.cydeo.dto.InvoiceDTO;

public interface InvoiceService {
    InvoiceDTO findById(Long id);
}
