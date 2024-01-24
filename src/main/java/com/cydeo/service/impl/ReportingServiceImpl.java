package com.cydeo.service.impl;

import com.cydeo.dto.InvoiceProductDTO;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.ReportingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportingServiceImpl implements ReportingService {
    private final InvoiceProductService invoiceProductService;
    public List<InvoiceProductDTO> getInvoiceProductList() {
      return invoiceProductService.findAllApprovedInvoiceInvoiceProduct(InvoiceStatus.APPROVED);
    }
}
