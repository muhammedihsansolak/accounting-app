package com.cydeo.repository;

import com.cydeo.dto.InvoiceDTO;
import com.cydeo.entity.Invoice;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct,Long> {
    List<InvoiceProduct> findByInvoiceId(Long invoiceId);

    boolean existsByProductId(Long productId);

    List<InvoiceProduct> findAllByInvoice_InvoiceStatus(InvoiceStatus invoiceStatus);

    List<InvoiceProduct> findByInvoice_Company_TitleAndProduct_NameAndInvoice_InvoiceTypeAndRemainingQuantityGreaterThanOrderByInsertDateTimeAsc(
            String companyName, String productName, InvoiceType invoiceType, int quantity);

    List<InvoiceProduct> findByInvoice_InvoiceTypeAndInvoice_CompanyAndInsertDateTimeStartsWith(
            InvoiceType invoiceType, String companyName,LocalDate startingDate);
}
