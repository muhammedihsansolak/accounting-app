package com.cydeo.service.impl;

import com.cydeo.dto.InvoiceProductDTO;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.ReportingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportingServiceImpl implements ReportingService {
    private final InvoiceProductService invoiceProductService;
    public List<InvoiceProductDTO> getInvoiceProductList() {
      return invoiceProductService.findAllApprovedInvoiceInvoiceProduct(InvoiceStatus.APPROVED);
    }

    @Override
    public Map<String, BigDecimal> getMonthlyProfitLossMap() {
        // Starting data of the company subscription, get the current user's company inserted day
        LocalDate startDate = LocalDate.of(2022, 3, 22);

        List<BigDecimal> profitLossData = invoiceProductService.getCumulativeTotalProfitLossUpToMonth(startDate);

        return null;
    }

//    public BigDecimal getCumulativeTotalProfitLossUpToMonth(LocalDate startDate) {
//        return invoiceProductRepository.getCumulativeTotalProfitLossUpToMonth(startDate);
//    }
//
//    // You can use this method to iterate through months and get the cumulative total profit/loss
//    public void printCumulativeTotalProfitLossForMonths() {
//        LocalDate startDate = LocalDate.of(2022, 3, 22); // Adjust the start date as needed
//
//        for (int month = 0; month < 12; month++) {
//            LocalDate currentDate = startDate.plusMonths(month);
//            BigDecimal cumulativeTotalProfitLoss = getCumulativeTotalProfitLossUpToMonth(currentDate);
//            System.out.println(String.format("%s %d cumulativeTotalProfitLoss = %s",
//                    getMonthName(currentDate.getMonthValue()), currentDate.getYear(), cumulativeTotalProfitLoss));
//        }
//    }
//
//    private String getMonthName(int month) {
//        // You can implement logic to get the month name based on the month number
//        // For simplicity, assuming you have a utility method for this
//        return MonthNameUtility.getMonthName(month);
//    }
//}
}
