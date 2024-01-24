package com.cydeo.service.impl;
import com.cydeo.dto.InvoiceProductDTO;
import com.cydeo.dto.ProductDTO;
import com.cydeo.entity.Invoice;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.entity.Product;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.InvoiceProductRepository;
import com.cydeo.repository.InvoiceRepository;
import com.cydeo.repository.ProductRepository;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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

    @Autowired
    private ProductRepository productRepository;

    /*
    ************** findById() **************
     */
    @Test
    void should_find_invoice_product_with_id_and_return_as_dto(){
        //given
        InvoiceProduct invoiceProduct = new InvoiceProduct();
        repository.save(invoiceProduct);

        //when
        InvoiceProductDTO invoiceProductDTO = invoiceProductService.findById(invoiceProduct.id);

        //then
        assertThat(invoiceProductDTO).isNotNull();
    }

    /*
     ************** findByInvoiceId() **************
     */
    @Test
    void should_find_invoice_products_based_on_invoice_id_and_calculate_tax_amount_and_total_amount(){
        //given
        Invoice invoice = new Invoice();
        invoiceRepository.save(invoice);

        InvoiceProduct invoiceProduct = new InvoiceProduct();
        invoiceProduct.setTax(10);
        invoiceProduct.setPrice(BigDecimal.valueOf(150));
        invoiceProduct.setQuantity(1);
        invoiceProduct.setInvoice(invoice);
        repository.save(invoiceProduct);

        //when
        List<InvoiceProductDTO> invoiceProductList = invoiceProductService.findByInvoiceId(invoice.id);

        //then
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
        //given
        Invoice invoice = new Invoice();
        invoiceRepository.save(invoice);

        InvoiceProduct invoiceProduct = new InvoiceProduct();
        invoiceProduct.setTax(10);
        invoiceProduct.setPrice(BigDecimal.valueOf(150));
        invoiceProduct.setQuantity(1);
        invoiceProduct.setInvoice(invoice);
        repository.save(invoiceProduct);

        //when
        invoiceProductService.removeInvoiceProductFromInvoice(invoice.id, invoiceProduct.id);
        Optional<InvoiceProduct> deletedInvoiceProduct = repository.findById(invoiceProduct.id);//returns null since @Where annotation

        //then
        assertThat(deletedInvoiceProduct).isPresent();
        assertThat(deletedInvoiceProduct.get().getIsDeleted()).isTrue();
    }

    /*
     ************** create() **************
     */
    @Test
    void should_create_invoice_product_and_set_related_invoice(){
        // Given
        Invoice invoice = new Invoice();
        invoiceRepository.save(invoice);

        Product product = new Product();
        Product savedProduct = productRepository.save(product);
        ProductDTO convertedProduct = mapper.convert(savedProduct, new ProductDTO());

        InvoiceProductDTO invoiceProductDTO = new InvoiceProductDTO();
        invoiceProductDTO.setQuantity(1);
        invoiceProductDTO.setPrice(BigDecimal.valueOf(100));
        invoiceProductDTO.setTax(10);
        invoiceProductDTO.setProduct(convertedProduct);

        //when
        InvoiceProductDTO created = invoiceProductService.create(invoiceProductDTO, invoice.id);

        //then
        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();
        assertThat(created.getInvoice().getId()).isEqualTo(invoice.id);
    }

    /*
     ************** doesProductHaveEnoughStock() **************
     */
    @Test
    void product_do_not_have_enough_stock() {
        // Given
        Product product = new Product();
        product.setName("Phone");
        product.setQuantityInStock(0); // No stock available
        Product savedProduct = productRepository.save(product);
        ProductDTO convertedProduct = mapper.convert(savedProduct, new ProductDTO());

        InvoiceProductDTO invoiceProductDTO = new InvoiceProductDTO();
        invoiceProductDTO.setQuantity(1); // Requesting 1 item
        invoiceProductDTO.setProduct(convertedProduct);

        BindingResult bindingResult = new BeanPropertyBindingResult(invoiceProductDTO, "invoiceProductDTO");

        // When
        BindingResult result = invoiceProductService.doesProductHaveEnoughStock(invoiceProductDTO, bindingResult);

        // Then
        assertTrue(result.hasErrors());
        assertEquals(1, result.getErrorCount());
        assertNotNull(result.getFieldError("product"));
        assertEquals("Product " + product.getName() + " has no enough stock!", result.getFieldError("product").getDefaultMessage());
    }

    @Test
    void product_have_stock(){
        // Given
        Product product = new Product();
        product.setName("Phone");
        product.setQuantityInStock(10); // 10 stock available
        Product savedProduct = productRepository.save(product);
        ProductDTO convertedProduct = mapper.convert(savedProduct, new ProductDTO());

        InvoiceProductDTO invoiceProductDTO = new InvoiceProductDTO();
        invoiceProductDTO.setQuantity(1); // Requesting 1 item
        invoiceProductDTO.setProduct(convertedProduct);

        BindingResult bindingResult = new BeanPropertyBindingResult(invoiceProductDTO, "invoiceProductDTO");

        // When
        BindingResult result = invoiceProductService.doesProductHaveEnoughStock(invoiceProductDTO, bindingResult);

        //then
        assertFalse(result.hasErrors());
    }

}
