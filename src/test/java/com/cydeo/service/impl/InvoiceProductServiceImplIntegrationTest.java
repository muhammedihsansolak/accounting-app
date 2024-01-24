package com.cydeo.service.impl;
import com.cydeo.dto.InvoiceDTO;
import com.cydeo.dto.InvoiceProductDTO;
import com.cydeo.dto.ProductDTO;
import com.cydeo.entity.*;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.*;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
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
    private InvoiceProductService invoiceProductService;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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

    /*
     ************** doesProductHasInvoice() **************
     */
    @Test
    void should_return_true_when_product_has_invoice(){
        //given
        Invoice invoice = new Invoice();
        Invoice savedInvoice = invoiceRepository.save(invoice);
        InvoiceDTO convertedInvoice = mapper.convert(savedInvoice, new InvoiceDTO());

        Product product = new Product();
        Product savedProduct = productRepository.save(product);
        ProductDTO convertedProduct = mapper.convert(savedProduct, new ProductDTO());

        InvoiceProductDTO invoiceProductDTO = new InvoiceProductDTO();
        invoiceProductDTO.setInvoice(convertedInvoice);
        invoiceProductDTO.setQuantity(1);
        invoiceProductDTO.setPrice(BigDecimal.valueOf(100));
        invoiceProductDTO.setTax(10);
        invoiceProductDTO.setProduct(convertedProduct);
        InvoiceProduct savedInvoiceProduct = mapper.convert(invoiceProductDTO, new InvoiceProduct());
        repository.save(savedInvoiceProduct);

        //when
        boolean doesProductHasInvoice = invoiceProductService.doesProductHasInvoice(savedProduct.id);

        assertTrue(doesProductHasInvoice);
    }

    /*
     ************** deleteByInvoice() **************
     */
    @Test
    void should_delete_all_invoice_products_related_to_given_invoice(){
        //given
        Invoice invoice1 = new Invoice();
        Invoice invoice2 = new Invoice();
        invoiceRepository.saveAll(List.of(invoice1,invoice2));

        InvoiceDTO convertedInvoice1 = mapper.convert(invoice1, new InvoiceDTO());
        InvoiceDTO convertedInvoice2 = mapper.convert(invoice2, new InvoiceDTO());

        Product product1 = new Product();
        Product savedProduct1 = productRepository.save(product1);
        ProductDTO convertedProduct1 = mapper.convert(savedProduct1, new ProductDTO());

        Product product2 = new Product();
        Product savedProduct2 = productRepository.save(product2);
        ProductDTO convertedProduct2 = mapper.convert(savedProduct2, new ProductDTO());

        InvoiceProductDTO invoiceProductDTO1 = new InvoiceProductDTO();
        invoiceProductDTO1.setInvoice(convertedInvoice1);
        invoiceProductDTO1.setQuantity(1);
        invoiceProductDTO1.setPrice(BigDecimal.valueOf(100));
        invoiceProductDTO1.setTax(10);
        invoiceProductDTO1.setProduct(convertedProduct1);
        InvoiceProduct convertedInvoiceProduct1 = mapper.convert(invoiceProductDTO1, new InvoiceProduct());
        InvoiceProduct savedInvoiceProduct1 = repository.save(convertedInvoiceProduct1);

        InvoiceProductDTO invoiceProductDTO2 = new InvoiceProductDTO();
        invoiceProductDTO2.setInvoice(convertedInvoice2);
        invoiceProductDTO2.setQuantity(1);
        invoiceProductDTO2.setPrice(BigDecimal.valueOf(100));
        invoiceProductDTO2.setTax(10);
        invoiceProductDTO2.setProduct(convertedProduct2);
        InvoiceProduct convertedInvoiceProduct2 = mapper.convert(invoiceProductDTO2, new InvoiceProduct());
        InvoiceProduct savedInvoiceProduct2 = repository.save(convertedInvoiceProduct2);

        //when
        invoiceProductService.deleteByInvoice(convertedInvoice1);
        Optional<InvoiceProduct> savedInvoiceProduct1Result = repository.findById(savedInvoiceProduct1.id);
        Optional<InvoiceProduct> savedInvoiceProduct2Result = repository.findById(savedInvoiceProduct2.id);//returns null since @Where annotation

        //then
        assertThat(savedInvoiceProduct2Result).isPresent();
        assertFalse(savedInvoiceProduct2Result.get().getIsDeleted());//should not be deleted

        assertThat(savedInvoiceProduct1Result).isPresent();
        assertTrue(savedInvoiceProduct1Result.get().getIsDeleted());//should deleted
    }

    /*
     ************** findAllApprovedInvoiceInvoiceProduct() **************
     */
    @Test
    @Transactional
    void should_find_all_invoice_products_which_belongs_to_approved_invoice() {
        // Given
        Invoice invoiceApproved = new Invoice();
        invoiceApproved.setInvoiceStatus(InvoiceStatus.APPROVED);
        invoiceApproved = invoiceRepository.save(invoiceApproved);

        Company company = new Company();
        Category category = new Category();
        category.setCompany(company);
        category = categoryRepository.save(category);

        Product product1 = new Product();
        product1.setCategory(category);
        product1 = productRepository.save(product1);

        InvoiceProduct invoiceProductApproved = new InvoiceProduct();
        invoiceProductApproved.setProduct(product1);
        invoiceProductApproved.setInvoice(invoiceApproved);
        invoiceProductApproved = repository.save(invoiceProductApproved);
        InvoiceProductDTO result = mapper.convert(invoiceProductApproved, new InvoiceProductDTO());

        // When
        List<InvoiceProductDTO> resultList = invoiceProductService.findAllApprovedInvoiceInvoiceProduct(InvoiceStatus.APPROVED);

        // Then
        assertThat(resultList).isNotNull();
        assertThat(resultList.contains(result));
    }

    @Test
    @Transactional
    void should_find_all_invoice_products_which_belongs_to_awaiting_approved_invoice() {
        // Given
        Invoice Awaiting = new Invoice();
        Awaiting.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        Awaiting = invoiceRepository.save(Awaiting);

        Company company = new Company();
        Category category = new Category();
        category.setCompany(company);
        category = categoryRepository.save(category);

        Product product1 = new Product();
        product1.setCategory(category);
        product1 = productRepository.save(product1);

        InvoiceProduct invoiceProduct = new InvoiceProduct();
        invoiceProduct.setProduct(product1);
        invoiceProduct.setInvoice(Awaiting);
        invoiceProduct = repository.save(invoiceProduct);
        InvoiceProductDTO result = mapper.convert(invoiceProduct, new InvoiceProductDTO());

        // When
        List<InvoiceProductDTO> resultList = invoiceProductService.findAllApprovedInvoiceInvoiceProduct(InvoiceStatus.AWAITING_APPROVAL);

        // Then
        assertThat(resultList).isNotNull();
        assertThat(resultList.contains(result));
    }


}
