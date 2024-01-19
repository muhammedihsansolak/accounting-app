package com.cydeo.controller;

import com.cydeo.dto.*;
import com.cydeo.enums.ClientVendorType;
import com.cydeo.enums.InvoiceType;
import com.cydeo.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/salesInvoices")
public class SalesInvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;
    private final ClientVendorService clientVendorService;
    private final ProductService productService;

    /**
     * Lists all sales invoices in the sales-invoice-list page
     */
    @GetMapping("/list")
    public String listAllSalesInvoices(Model model){
        List<InvoiceDTO> invoiceDTOList = invoiceService.findAllInvoices(InvoiceType.SALES);

        model.addAttribute("invoices", invoiceDTOList);

        return "invoice/sales-invoice-list";
    }

    /**
     * When end-user click "Edit" button they should land on sales-invoice-update page. This section is the place end-user will add products in that Invoice,
     */
    @GetMapping("/update/{id}")
    public String editInvoice(@PathVariable("id")Long id, Model model){
        InvoiceDTO foundInvoice = invoiceService.findById(id);
        List<InvoiceProductDTO> invoiceProductDTOList = invoiceProductService.findByInvoiceId(id);
        List<ClientVendorDTO> clientVendorDTOList = clientVendorService.findClientVendorByClientVendorTypeAndCompany(ClientVendorType.CLIENT);

        model.addAttribute("invoice",foundInvoice);
        model.addAttribute("newInvoiceProduct", new InvoiceProductDTO());
        model.addAttribute("products", productService.listAllProducts()); //TODO List All By Company
        model.addAttribute("invoiceProducts", invoiceProductDTOList);
        model.addAttribute("clients", clientVendorDTOList );

        return "invoice/sales-invoice-update";
    }

    /**
     * When end-user click "Save" button, invoice should be updated and they should land on sales-invoice-list page. When click on Add Product button, this product (InvoiceProduct actually) should be saved to database as an InvoiceProduct, and end-user should be redirected to the very same page with updated Product List section below (Invoice Products actually)
     */
    @PostMapping("/update/{id}")
    public String updateInvoice(@PathVariable("id")Long id, @Valid @ModelAttribute("invoice")InvoiceDTO invoiceToUpdate , BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return "redirect:/salesInvoices/update/"+id;
        }

        InvoiceDTO foundInvoice = invoiceService.findById(id);

        invoiceService.update(foundInvoice, invoiceToUpdate);

        return "redirect:/salesInvoices/update/"+id;
    }

    @PostMapping("/addInvoiceProduct/{id}")
    public String addInvoiceProduct(@Valid @ModelAttribute("newInvoiceProduct")InvoiceProductDTO invoiceProductDTO, BindingResult bindingResult, @PathVariable("id")Long id, Model model){

        bindingResult = invoiceProductService.doesProductHaveEnoughStock(invoiceProductDTO, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            InvoiceDTO foundInvoice = invoiceService.findById(id);
            List<InvoiceProductDTO> invoiceProductDTOList = invoiceProductService.findByInvoiceId(id);
            List<ClientVendorDTO> clientVendorDTOList = clientVendorService.findClientVendorByClientVendorTypeAndCompany(ClientVendorType.CLIENT);

            model.addAttribute("invoice",foundInvoice);
            model.addAttribute("products", productService.listAllProducts());
            model.addAttribute("invoiceProducts", invoiceProductDTOList);
            model.addAttribute("clients", clientVendorDTOList );

            return "invoice/sales-invoice-update";
        }

        invoiceProductService.create(invoiceProductDTO, id);

        return "redirect:/salesInvoices/update/"+id;
    }

    /**
     * When end-user click "Delete" button, invoice should be deleted (soft delete) and they should land on sales-invoice-list page
     */
    @GetMapping("/delete/{id}")
    public String deleteInvoice(@PathVariable("id")Long invoiceId){

        invoiceService.deleteInvoice(invoiceId);

        return "redirect:/salesInvoices/list";
    }

    /**
     * When End-user clicks on the "Approve" button, invoice status should be converted to "Approved" and they should land on sales-invoice-list page
     */
    @GetMapping("/approve/{id}")
    public String approveInvoice(@PathVariable("id")Long invoiceId){

        invoiceService.approve(invoiceId);

        return "redirect:/salesInvoices/list";
    }

    /**
     * When End-User clicks on "Create Sales Invoice" button, sales_invoice_create page should be displayed
     */
    @GetMapping("/create")
    public String createInvoice(Model model){
        InvoiceDTO invoice = invoiceService.invoiceGenerator(InvoiceType.SALES);
        List<ClientVendorDTO> clientVendorDTOList = clientVendorService.findClientVendorByClientVendorTypeAndCompany(ClientVendorType.CLIENT);

        model.addAttribute("newSalesInvoice", invoice);
        model.addAttribute("clients", clientVendorDTOList );

        return "invoice/sales-invoice-create";
    }

    /**
     * When End-user clicks on SAVE button, a new sales_invoice should be created in the database and end-user should land the sales_invoice_update page. (because we only created invoice, but there are no products in it... We need to add them in update page)
     */
    @PostMapping("/create")
    public String createInvoice(@Valid @ModelAttribute("newSalesInvoice") InvoiceDTO invoice, BindingResult bindingResult, Model model){

        if (bindingResult.hasErrors()){
            List<ClientVendorDTO> clientVendorDTOList = clientVendorService.findClientVendorByClientVendorTypeAndCompany(ClientVendorType.CLIENT);
            model.addAttribute("clients", clientVendorDTOList );

            return "invoice/sales-invoice-create";
        }

        InvoiceDTO createdInvoice = invoiceService.create(invoice, InvoiceType.SALES);

        return "redirect:/salesInvoices/update/"+createdInvoice.getId();
    }

    /**
     * When end-user clicks on "-" button, related invoice_product should be deleted from current Invoice.
     */
    @GetMapping("/removeInvoiceProduct/{invoiceId}/{invoiceProductId}")
    public String removeInvoiceProductFromInvoice(@PathVariable("invoiceId")Long invoiceId, @PathVariable("invoiceProductId")Long invoiceProductId){
        invoiceProductService.removeInvoiceProductFromInvoice(invoiceId, invoiceProductId);

        return "redirect:/salesInvoices/update/"+invoiceId;
    }

    /**
     * As a user, I should be able to print approved Sales Invoices
     */
    @GetMapping("/print/{invoiceId}")
    public String printSalesInvoice(@PathVariable("invoiceId")Long invoiceId , Model model){
        InvoiceDTO invoice = invoiceService.findById(invoiceId);
        List<InvoiceProductDTO> invoiceProductDTOList =  invoiceProductService.findByInvoiceId(invoiceId);

        model.addAttribute("invoice", invoice);
        model.addAttribute("company", invoice.getCompany());
        model.addAttribute("invoiceProducts", invoiceProductDTOList);

        return "invoice/invoice_print";
    }

}
