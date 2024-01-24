package com.cydeo.service.impl;

import com.cydeo.dto.InvoiceDTO;
import com.cydeo.dto.InvoiceProductDTO;
import com.cydeo.dto.ProductDTO;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.exception.InvoiceProductNotFoundException;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.InvoiceProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceProductServiceImplUnitTest {

    @Mock
    private InvoiceProductRepository repository;
    @Mock
    private MapperUtil mapper;
    @Mock
    private InvoiceServiceImpl invoiceService;

    @InjectMocks
    private InvoiceProductServiceImpl invoiceProductService;

    /*
     ************** findById() **************
     */
    @Test
    void should_throw_exception_when_invoice_product_not_found_with_id() {
        //given
        Long id = 1L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        //when
        Throwable throwable = catchThrowable(() -> invoiceProductService.findById(id));

        //then
        assertThat(throwable).isInstanceOf(InvoiceProductNotFoundException.class);
    }

    @Test
    void should_execute_mapper_at_least_once_and_should_return_what_is_converted() {
        //given
        Long id = 1L;
        InvoiceProduct invoiceProduct = new InvoiceProduct();
        InvoiceProductDTO invoiceProductDTO = new InvoiceProductDTO();

        when(repository.findById(id)).thenReturn(Optional.of(invoiceProduct));
        when(mapper.convert(invoiceProduct, new InvoiceProductDTO())).thenReturn(invoiceProductDTO);

        //when
        InvoiceProductDTO returnedInvoiceProduct = invoiceProductService.findById(id);

        //then
        verify(mapper, times(1)).convert(invoiceProduct, invoiceProductDTO);
        assertNotNull(returnedInvoiceProduct);
        assertSame(invoiceProductDTO, returnedInvoiceProduct);
    }

    /*
     ************** findByInvoiceId() **************
     */
    @Test
    void should_find_all_invoice_products_and_calculate_tax_and_total_amount() {
        // Given
        Long invoiceId = 1L;

        InvoiceProduct invoiceProduct1 = new InvoiceProduct();

        InvoiceProduct invoiceProduct2 = new InvoiceProduct();

        List<InvoiceProduct> invoiceProducts = Arrays.asList(invoiceProduct1, invoiceProduct2);

        InvoiceProductDTO invoiceProductDTO1 = new InvoiceProductDTO();
        invoiceProductDTO1.setQuantity(2);
        invoiceProductDTO1.setPrice(BigDecimal.valueOf(100));
        BigDecimal taxAmount1 = BigDecimal.valueOf(20);

        InvoiceProductDTO invoiceProductDTO2 = new InvoiceProductDTO();
        invoiceProductDTO2.setQuantity(1);
        invoiceProductDTO2.setPrice(BigDecimal.valueOf(200));
        BigDecimal taxAmount2 = BigDecimal.valueOf(40);

        when(repository.findByInvoiceId(invoiceId)).thenReturn(invoiceProducts);
        when(mapper.convert(invoiceProduct1, new InvoiceProductDTO())).thenReturn(invoiceProductDTO1);
        when(mapper.convert(invoiceProduct2, new InvoiceProductDTO())).thenReturn(invoiceProductDTO2);
        when(invoiceService.calculateTaxForProduct(invoiceProductDTO1)).thenReturn(taxAmount1);
        when(invoiceService.calculateTaxForProduct(invoiceProductDTO2)).thenReturn(taxAmount2);

        // When
        List<InvoiceProductDTO> result = invoiceProductService.findByInvoiceId(invoiceId);

        // Then
        verify(repository, atLeastOnce()).findByInvoiceId(invoiceId);
        verify(mapper).convert(invoiceProduct1, new InvoiceProductDTO());
        verify(mapper).convert(invoiceProduct2, new InvoiceProductDTO());
        verify(invoiceService).calculateTaxForProduct(invoiceProductDTO1);
        verify(invoiceService).calculateTaxForProduct(invoiceProductDTO2);

        assertEquals(2, result.size()); // Check the size of the result list
        // Assuming that the total amount is calculated and set in the DTO
        assertEquals(BigDecimal.valueOf(220), invoiceProductDTO1.getTotal()); // Total = 100 * 2 + 20
        assertEquals(BigDecimal.valueOf(240), invoiceProductDTO2.getTotal()); // Total = 200 * 1 + 40
    }

    /*
     ************** findByInvoiceId() **************
     */
    @Test
    void should_throw_exception_when_invoice_product_can_not_found_with_id() {
        //given
        Long id = 1L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        //when
        Throwable throwable = catchThrowable(() -> invoiceProductService.deleteById(id));

        //then
        assertThat(throwable).isInstanceOf(InvoiceProductNotFoundException.class);
    }

    /*
     ************** deleteById() **************
     */
    @Test
    void should_sets_is_deleted_field_to_true() {
        //given
        Long id = 1L;

        InvoiceProduct invoiceToDelete = new InvoiceProduct();
        invoiceToDelete.setIsDeleted(Boolean.FALSE);

        InvoiceProduct deleted = new InvoiceProduct();
        deleted.setIsDeleted(Boolean.TRUE);

        InvoiceProductDTO invoiceProductDTO = new InvoiceProductDTO();

        when(repository.findById(id)).thenReturn(Optional.of(invoiceToDelete));
        when(repository.save(invoiceToDelete)).thenReturn(deleted);
        when(mapper.convert(any(InvoiceProduct.class), any(InvoiceProductDTO.class))).thenReturn(invoiceProductDTO);

        //when
        InvoiceProductDTO result = invoiceProductService.deleteById(id);

        //then
        verify(repository).findById(id);
        verify(repository).save(invoiceToDelete);
        verify(mapper).convert(deleted, new InvoiceProductDTO());
        assertNotNull(result);
    }

    /*
     ************** removeInvoiceProductFromInvoice() **************
     */
    //TODO fix error
    @Test
    void should_remove_invoice_product_from_invoice() {
        // Given
        Long invoiceId = 1L;
        Long invoiceProductId = 2L;

        InvoiceProduct invoiceProduct1 = new InvoiceProduct();
        InvoiceProduct invoiceProduct2 = new InvoiceProduct();

        List<InvoiceProduct> invoiceProductList = new ArrayList<>();
        invoiceProductList.add(invoiceProduct1);
        invoiceProductList.add(invoiceProduct2);

        InvoiceProductDTO invoiceProductDTO1 = new InvoiceProductDTO();
        invoiceProductDTO1.setId(2L); // This product should be deleted
        invoiceProductDTO1.setPrice(BigDecimal.valueOf(50));
        invoiceProductDTO1.setTax(10);
        invoiceProductDTO1.setQuantity(1);

        InvoiceProductDTO invoiceProductDTO2 = new InvoiceProductDTO();
        invoiceProductDTO2.setId(3L); // This product should NOT be deleted
        invoiceProductDTO2.setPrice(BigDecimal.valueOf(100));
        invoiceProductDTO2.setTax(5);
        invoiceProductDTO2.setQuantity(1);

        List<InvoiceProductDTO> invoiceProductDTOList = new ArrayList<>();
        invoiceProductDTOList.add(invoiceProductDTO1);
        invoiceProductDTOList.add(invoiceProductDTO2);

        when(repository.findByInvoiceId(invoiceId)).thenReturn(invoiceProductList);
        when(mapper.convert(invoiceProductList.get(0), new InvoiceProductDTO())).thenReturn(invoiceProductDTO1);
        when(mapper.convert(invoiceProductList.get(1), new InvoiceProductDTO())).thenReturn(invoiceProductDTO2);
        when(invoiceService.calculateTaxForProduct(invoiceProductDTO1)).thenReturn(BigDecimal.valueOf(5));
        when(invoiceService.calculateTaxForProduct(invoiceProductDTO2)).thenReturn(BigDecimal.valueOf(5));
        when(repository.findById(2L)).thenReturn(Optional.of(invoiceProduct1));

        // When
        invoiceProductService.removeInvoiceProductFromInvoice(invoiceId, invoiceProductId);

        // Then
        verify(repository).findByInvoiceId(invoiceId);
        verify(repository).findById(invoiceProductId);
        verify(repository, never()).deleteById(3L); // Verifying that it's not called for invoiceProductDTO2
    }

    /*
     ************** create() **************
     */
    @Test
    void should_create_invoice_product(){
        //given
        Long invoiceId = 1L;

        InvoiceDTO invoiceDTO = new InvoiceDTO();
        InvoiceProduct invoiceProduct = new InvoiceProduct();
        InvoiceProductDTO invoiceProductDTO = new InvoiceProductDTO();
        InvoiceProduct savedInvoiceProduct = new InvoiceProduct();
        InvoiceProductDTO savedInvoiceProductDTO = new InvoiceProductDTO();

        when(invoiceService.findById(invoiceId)).thenReturn(invoiceDTO);
        when(mapper.convert(eq(invoiceProductDTO), any(InvoiceProduct.class))).thenReturn(invoiceProduct);
        when(repository.save(invoiceProduct)).thenReturn(savedInvoiceProduct);
        when(mapper.convert(eq(savedInvoiceProduct),  any(InvoiceProductDTO.class))).thenReturn(savedInvoiceProductDTO);

        // When
        invoiceProductService.create(invoiceProductDTO, invoiceId);

        // Then
        verify(invoiceService).findById(invoiceId);
        verify(mapper).convert(eq(invoiceProductDTO), any(InvoiceProduct.class));
        verify(repository).save(invoiceProduct);
        verify(mapper).convert(eq(savedInvoiceProduct), any(InvoiceProductDTO.class));
    }

    /*
     ************** doesProductHaveEnoughStock() **************
     */
    @Test
    void product_has_enough_stock(){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setQuantityInStock(15);

        InvoiceProductDTO invoiceProductDTO = new InvoiceProductDTO();
        invoiceProductDTO.setProduct(productDTO);
        invoiceProductDTO.setQuantity(10);//less than product stock

        BindingResult bindingResult = mock(BindingResult.class);

        BindingResult result = invoiceProductService.doesProductHaveEnoughStock(invoiceProductDTO, bindingResult);

        verify(bindingResult, never()).addError(any(ObjectError.class)); //if product has enough stock we DO NOT add error to bindingResult
    }

    @Test
    void product_do_not_have_enough_stock(){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setQuantityInStock(15);

        InvoiceProductDTO invoiceProductDTO = new InvoiceProductDTO();
        invoiceProductDTO.setProduct(productDTO);
        invoiceProductDTO.setQuantity(100);//more than product stock

        BindingResult bindingResult = mock(BindingResult.class);

        BindingResult result = invoiceProductService.doesProductHaveEnoughStock(invoiceProductDTO, bindingResult);

        verify(bindingResult, atLeastOnce()).addError(any(ObjectError.class)); //if product has enough stock we add error to bindingResult
    }

    /*
     ************** deleteByInvoice() **************
     */
    @Test
    void should_delete_invoice_products_belongs_to_invoice(){
        // Given
        Long invoiceId = 1L;
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setId(invoiceId);

        InvoiceProduct invoiceProduct1 = new InvoiceProduct();
        InvoiceProduct invoiceProduct2 = new InvoiceProduct();

        List<InvoiceProduct> invoiceProductList = Arrays.asList(invoiceProduct1, invoiceProduct2);

        when(repository.findByInvoiceId(invoiceId)).thenReturn(invoiceProductList);

        ArgumentCaptor<List<InvoiceProduct>> captor = ArgumentCaptor.forClass(List.class);

        // When
        invoiceProductService.deleteByInvoice(invoiceDTO);

        // Then
        verify(repository).findByInvoiceId(invoiceId);
        verify(repository).saveAll(captor.capture());

        List<InvoiceProduct> capturedInvoiceProducts = captor.getValue();
        assertTrue(capturedInvoiceProducts.stream().allMatch(InvoiceProduct::getIsDeleted));//assert that each InvoiceProduct in this list has isDeleted set to true.
    }

    /*
     ************** findAllApprovedInvoiceInvoiceProduct() **************
     */
    @Test
    void should_find_all_invoice_products_belongs_to_approved_invoices() {
        // Given
        InvoiceStatus approvedStatus = InvoiceStatus.APPROVED;
        List<InvoiceProduct> invoiceProducts = Arrays.asList(
                new InvoiceProduct(),
                new InvoiceProduct()
        );

        when(repository.findAllByInvoice_InvoiceStatus(approvedStatus)).thenReturn(invoiceProducts);

        InvoiceProductDTO invoiceProductDTO1 = new InvoiceProductDTO();
        InvoiceProductDTO invoiceProductDTO2 = new InvoiceProductDTO();

        when(mapper.convert(invoiceProducts.get(0), new InvoiceProductDTO())).thenReturn(invoiceProductDTO1);
        when(mapper.convert(invoiceProducts.get(1), new InvoiceProductDTO())).thenReturn(invoiceProductDTO2);

        // When
        List<InvoiceProductDTO> result = invoiceProductService.findAllApprovedInvoiceInvoiceProduct(approvedStatus);

        // Then
        assertEquals(2, result.size());
        assertSame(invoiceProductDTO1, result.get(0));
        assertSame(invoiceProductDTO2, result.get(1));
        verify(repository).findAllByInvoice_InvoiceStatus(approvedStatus);
        verify(mapper).convert(invoiceProducts.get(0), new InvoiceProductDTO());
        verify(mapper).convert(invoiceProducts.get(1), new InvoiceProductDTO());
    }


}