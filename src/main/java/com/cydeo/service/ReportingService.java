package com.cydeo.service;

import com.cydeo.dto.InvoiceProductDTO;

import java.math.BigDecimal;
import java.util.BitSet;
import java.util.List;
import java.util.Map;

public interface ReportingService {
    List<InvoiceProductDTO> getInvoiceProductList();

    Map<String, BigDecimal> getMonthlyProfitLossMap();
}
