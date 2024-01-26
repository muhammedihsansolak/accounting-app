package com.cydeo.service.impl;

import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import com.cydeo.service.DashboardService;
import com.cydeo.service.InvoiceService;
import com.cydeo.service.ReportingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final InvoiceService invoiceService;
    private final ReportingService reportingService;

    public DashboardServiceImpl(InvoiceService invoiceService, ReportingService reportingService) {
        this.invoiceService = invoiceService;
        this.reportingService = reportingService;
    }

    @Override
    public Map<String,BigDecimal> summaryNumbersCalculation() {

        Map<String, BigDecimal> summaryNumbers = new HashMap<>();

         //Calculate total coast
     BigDecimal totalCost = invoiceService.findAllInvoices(InvoiceType.PURCHASE ).stream()
                .filter(invoiceDTO -> invoiceDTO.getInvoiceStatus()== InvoiceStatus.APPROVED)
                .map(invoiceDTO -> invoiceDTO.getTotal()).reduce(BigDecimal.ZERO,BigDecimal::add);

       //Calculate total sales
       BigDecimal totalSales =  invoiceService.findAllInvoices(InvoiceType.SALES).stream()
                .filter(invoiceDTO -> invoiceDTO.getInvoiceStatus()==InvoiceStatus.APPROVED)
                .map(invoiceDTO -> invoiceDTO.getTotal()).reduce(BigDecimal.ZERO,BigDecimal::add);

       //Calculate profit/loss
       //BigDecimal profitLoss = totalSales.subtract(totalCost);
        List<Map.Entry<String ,BigDecimal>> monthlyProfitLossList= reportingService.getMonthlyProfitLossListMap();
        BigDecimal totalProfitLoss = BigDecimal.ZERO;

        for (Map.Entry<String, BigDecimal> entry : monthlyProfitLossList) {
            BigDecimal profitLoss = entry.getValue();
            totalProfitLoss = totalProfitLoss.add(profitLoss);
        }


        summaryNumbers.put("totalCost",totalCost);
        summaryNumbers.put("totalSales",totalSales);
        summaryNumbers.put("profitLoss",totalProfitLoss);

        return summaryNumbers;

    }



}
