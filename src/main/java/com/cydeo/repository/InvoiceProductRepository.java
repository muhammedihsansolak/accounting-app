package com.cydeo.repository;

import com.cydeo.dto.InvoiceDTO;
import com.cydeo.entity.Company;
import com.cydeo.entity.Invoice;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct,Long> {
    List<InvoiceProduct> findByInvoiceId(Long invoiceId);

    boolean existsByProductId(Long productId);

    List<InvoiceProduct> findAllByInvoice_InvoiceStatus(InvoiceStatus invoiceStatus);

    List<InvoiceProduct> findByInvoice_CompanyAndInvoice_InvoiceStatus(Company company, InvoiceStatus invoiceStatus);
}
