package com.cydeo.service.impl;

import com.cydeo.dto.CompanyDTO;
import com.cydeo.dto.InvoiceProductDTO;
import com.cydeo.dto.ProductDTO;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import com.cydeo.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportingServiceImpl implements ReportingService {
    private final InvoiceProductService invoiceProductService;
    private final SecurityService securityService;
    private final CompanyService companyService;
    private final ProductService productService;

    public List<InvoiceProductDTO> getInvoiceProductList() {
      return invoiceProductService.findAllApprovedInvoiceInvoiceProduct(InvoiceStatus.APPROVED);
    }

    @Override
    @Transactional
    public List<Map.Entry<String ,BigDecimal>> getMonthlyProfitLossListMap() {

        List<Map.Entry<String ,BigDecimal>> listOfMap = new ArrayList<>();

        // Starting data of the company subscription, get the current user's company inserted day
        String companyName = securityService.getLoggedInUser().getCompany().getTitle();
        CompanyDTO currentCompany = companyService.findByCompanyTitle(companyName);

        LocalDateTime startDate = currentCompany.getInsertDateTime();

        List<LocalDate> keys = mapKeyGenerator(startDate);

        for (LocalDate currentKey : keys) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMMM");
            String key = currentKey.format(formatter);

            BigDecimal profitLoss = invoiceProductService
                    .getProfitLossBasedOneMonth(currentKey.getYear(), currentKey.getMonthValue()
                            , currentCompany.getId(), InvoiceType.SALES);

            listOfMap.add(Map.entry(key.toUpperCase(),profitLoss));
        }
        return listOfMap;
    }

    @Override
    public List<Map.Entry<String, BigDecimal>> getProductProfitLossListMap() {

        List<Map.Entry<String ,BigDecimal>> listOfMap = new ArrayList<>();
        // get the current company id;
        Long companyId = securityService.getLoggedInUser().getCompany().getId();

        List<ProductDTO> allProducts = productService.listAllProducts();

        for (ProductDTO product : allProducts) {
            BigDecimal profitLoss = invoiceProductService
                    .getProductProfitLoss(product.getId(), companyId);
            listOfMap.add(Map.entry(product.getName(),profitLoss));
        }
        return listOfMap;
    }

    private List<LocalDate> mapKeyGenerator(LocalDateTime localDate) {
        int startYear = localDate.getYear();
        Month startMonth = localDate.getMonth();
        int startDay = localDate.getDayOfMonth();

        LocalDate startDate = LocalDate.of(startYear, startMonth, startDay);
        List<LocalDate> mapKeys = new ArrayList<>();
        LocalDate now = LocalDate.now();

        while (startDate.isBefore(now)) {
            mapKeys.add(now);
            now = now.minusMonths(1);  // Use minusMonths for descending order
        }
        return mapKeys;
    }



}
