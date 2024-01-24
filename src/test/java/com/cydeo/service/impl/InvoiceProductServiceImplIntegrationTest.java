package com.cydeo.service.impl;
import com.cydeo.dto.InvoiceProductDTO;
import com.cydeo.entity.Invoice;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.InvoiceProductRepository;
import com.cydeo.repository.InvoiceRepository;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class InvoiceProductServiceImplIntegrationTest {

    @Autowired
    private InvoiceProductRepository repository;

    @Autowired
    private MapperUtil mapper;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoiceProductService invoiceProductService;

    @Autowired
    private InvoiceRepository invoiceRepository;

    /*
    ************** findById() **************
     */
    @Test
    void should_find_invoice_product_with_id_and_return_as_dto(){
        InvoiceProduct invoiceProduct = new InvoiceProduct();
        repository.save(invoiceProduct);

        InvoiceProductDTO invoiceProductDTO = invoiceProductService.findById(invoiceProduct.id);

        assertThat(invoiceProductDTO).isNotNull();
    }

    /*
     ************** findByInvoiceId() **************
     */
    @Test
    void should_find_invoice_products_based_on_invoice_id_and_calculate_tax_amount_and_total_amount(){
        Invoice invoice = new Invoice();
        invoiceRepository.save(invoice);

        InvoiceProduct invoiceProduct = new InvoiceProduct();
        invoiceProduct.setTax(10);
        invoiceProduct.setPrice(BigDecimal.valueOf(150));
        invoiceProduct.setQuantity(1);
        invoiceProduct.setInvoice(invoice);
        repository.save(invoiceProduct);

        List<InvoiceProductDTO> invoiceProductList = invoiceProductService.findByInvoiceId(invoice.id);

        assertThat(invoiceProductList).isNotNull();
        assertThat(invoiceProductList.get(0)).isNotNull();
        assertThat(invoiceProductList.get(0).getTotal()).isEqualTo(BigDecimal.valueOf(165).setScale(2));
    }

    /*
     ************** deleteById() **************
     */
    @Test
    void should_softly_delete_the_given_invoice_product() {
        //given
        InvoiceProduct invoiceProduct = new InvoiceProduct();
        InvoiceProduct saved = repository.save(invoiceProduct);


        assertFalse(saved.getIsDeleted());

        // When
        InvoiceProductDTO invoiceProductDTO = invoiceProductService.deleteById(saved.getId());
        Optional<InvoiceProduct> deletedInvoiceProduct = repository.findById(invoiceProductDTO.getId());//returns null since @Where annotation

        // Then
        assertThat(invoiceProductDTO).isNotNull();
        assertThat(deletedInvoiceProduct).isPresent();
        assertThat(deletedInvoiceProduct.get().getIsDeleted()).isTrue();
    }

    /*
     ************** removeInvoiceProductFromInvoice() **************
     */
    @Test
    void should_remove_invoice_product_from_invoice(){
        Invoice invoice = new Invoice();
        invoiceRepository.save(invoice);

        InvoiceProduct invoiceProduct = new InvoiceProduct();
        invoiceProduct.setTax(10);
        invoiceProduct.setPrice(BigDecimal.valueOf(150));
        invoiceProduct.setQuantity(1);
        invoiceProduct.setInvoice(invoice);
        repository.save(invoiceProduct);

        invoiceProductService.removeInvoiceProductFromInvoice(invoice.id, invoiceProduct.id);
        Optional<InvoiceProduct> deletedInvoiceProduct = repository.findById(invoiceProduct.id);//returns null since @Where annotation

        assertThat(deletedInvoiceProduct).isPresent();
        assertThat(deletedInvoiceProduct.get().getIsDeleted()).isTrue();
    }

}
