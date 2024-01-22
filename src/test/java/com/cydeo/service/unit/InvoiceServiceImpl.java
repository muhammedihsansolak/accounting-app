package com.cydeo.service.unit;

import com.cydeo.dto.CompanyDTO;
import com.cydeo.dto.InvoiceDTO;
import com.cydeo.dto.InvoiceProductDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Company;
import com.cydeo.entity.Invoice;
import com.cydeo.enums.InvoiceType;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.InvoiceRepository;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import com.cydeo.service.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class InvoiceServiceImpl {


    @Mock
    private  InvoiceRepository invoiceRepository;
    @Mock
    private  MapperUtil mapper;
    @InjectMocks
    private  SecurityService securityService;
    @InjectMocks
    private  InvoiceProductService invoiceProductService;
    @Mock
    private InvoiceService invoiceService;


    UserDTO user;
    Invoice invoice;


    @BeforeEach   //we will create sample data for each method
    public UserDTO getUser() {

        user = new UserDTO();
        CompanyDTO company = new CompanyDTO();
        company.setTitle("Test company");
        user.setCompany(company);
        return user;
    }

@BeforeEach
    public List<Invoice> getInvoiceList() {
        Invoice invoice1 = new Invoice();
        invoice1.setId(1L);
        invoice1.setInvoiceNo("Invoice-001");

        Invoice invoice2 = new Invoice();
        invoice1.setId(2L);
        invoice1.setInvoiceNo("Invoice-002");

        List<Invoice> invoiceList = Arrays.asList(invoice1,invoice2);
        return invoiceList;
    }

@BeforeEach
    public List<InvoiceProductDTO> getInvoiceProductList() {
        List<InvoiceProductDTO> invoiceProductDTOList1 = Arrays.asList(/* ... */);
       // List<InvoiceProductDTO> invoiceProductDTOList2 = Arrays.asList(/* ... */);
        return invoiceProductDTOList1;
    }

    @BeforeEach
    void mapper(){
        // Mock behavior for the mapper
        when(mapper.convert(any(), any())).thenAnswer(mappingObj -> {
            Invoice invoice = mappingObj.getArgument(0);
            InvoiceDTO invoiceDTO = new InvoiceDTO();
            invoiceDTO.setId(invoice.getId());
            return invoiceDTO;
        });

    }

    @Test
    void should_list_all_invoices(){

        //given-Preparation


        //when-Action
        when(securityService.getLoggedInUser()).thenReturn(getUser());
        when(invoiceRepository.findInvoiceByInvoiceTypeAndCompany_TitleAndIsDeletedOrderByInvoiceNoDesc(any(),any(),any())).thenReturn(getInvoiceList());
        when(mapper.convert(any(),any()));

        when(invoiceProductService.findByInvoiceId(1L)).thenReturn(getInvoiceProductList());

        //then- Assertion/Verification

   List<Invoice> expected = getInvoiceList();


    }


}
