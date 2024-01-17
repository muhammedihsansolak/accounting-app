package com.cydeo.service.impl;

import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import com.cydeo.service.DashboardService;
import com.cydeo.service.InvoiceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final InvoiceService invoiceService;

    public DashboardServiceImpl(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Override
    public BigDecimal totalCost() {

       return invoiceService.findAllInvoices(InvoiceType.PURCHASE ).stream()
                .filter(invoiceDTO -> invoiceDTO.getInvoiceStatus()== InvoiceStatus.APPROVED)
                .map(invoiceDTO -> invoiceDTO.getTotal()).reduce(BigDecimal.ZERO,BigDecimal::add);

    }

    @Override
    public BigDecimal totalSales() {
        return invoiceService.findAllInvoices(InvoiceType.SALES).stream()
                .filter(invoiceDTO -> invoiceDTO.getInvoiceStatus()==InvoiceStatus.APPROVED)
                .map(invoiceDTO -> invoiceDTO.getTotal()).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    @Override
    public BigDecimal profitLoss() {

  return totalSales().subtract(totalCost());


    }
}
