package com.cydeo.dto;

import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class InvoiceDTO {

    private Long id;

    private String invoiceNo;

    private InvoiceStatus invoiceStatus;

    private InvoiceType invoiceType;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private CompanyDTO company;

    private ClientVendorDTO clientVendor;

    private BigDecimal price;

    private BigDecimal tax;

    private BigDecimal total;






}
