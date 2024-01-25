package com.cydeo.service;

import com.cydeo.dto.InvoiceDTO;
import com.cydeo.dto.InvoiceProductDTO;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface InvoiceProductService {
    InvoiceProductDTO findById(Long id);
    List<InvoiceProductDTO> findByInvoiceId(Long invoiceId);
    List<InvoiceProductDTO> findByInvoiceIdAndTotalCalculated(Long invoiceId);
    InvoiceDTO deleteById(Long id);
    void removeInvoiceProductFromInvoice(Long invoiceId, Long invoiceProductId);

    InvoiceProductDTO create(InvoiceProductDTO invoiceProductDTO, Long invoiceId);

    BindingResult doesProductHaveEnoughStock(InvoiceProductDTO invoiceProductDTO, BindingResult bindingResult );

    boolean doesProductHasInvoice(Long productId);
    List<InvoiceProductDTO> findAll();

    void deleteByInvoice(InvoiceDTO invoice);
    List<InvoiceProductDTO> findAllApprovedInvoiceInvoiceProduct(InvoiceStatus invoiceStatus);

    void save(InvoiceProductDTO invoiceProductDTO);

    List<InvoiceProductDTO> getPerchesInvoiceProductsListQuantityNotZero(
            String companyName, String productName, InvoiceType invoiceType, int quantity);
}
