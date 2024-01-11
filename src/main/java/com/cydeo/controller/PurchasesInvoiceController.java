package com.cydeo.controller;

import com.cydeo.dto.*;
import com.cydeo.enums.InvoiceType;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import com.cydeo.service.SecurityService;
import com.cydeo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/purchaseInvoices")
public class PurchasesInvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;
    private final UserService userService;
    private final SecurityService securityService;

    /**
     * Lists all purchase invoices in the purchase-invoice-list page
     */
    @GetMapping("/list")
    public String listAllPurchaseInvoices(Model model){
        List<InvoiceDTO> invoiceDTOList = invoiceService.findAllPurchaseInvoices();

        model.addAttribute("invoices", invoiceDTOList);

        return "invoice/purchase-invoice-list";
    }

    /**
     * When end-user click "Edit" button they should land on purchase-invoice-update page. This section is the place end-user will add products in that Invoice,
     */
    @GetMapping("/update/{id}")
    public String editInvoiceProduct(@PathVariable("id")Long id, Model model){
        InvoiceDTO foundInvoice = invoiceService.findById(id);
        List<InvoiceProductDTO> invoiceProductDTOList = invoiceProductService.findByInvoiceId(id);

        model.addAttribute("invoice",foundInvoice);
        model.addAttribute("newInvoiceProduct", new InvoiceProductDTO());
        model.addAttribute("products", List.of(new ProductDTO())); //TODO implement productService
        model.addAttribute("invoiceProducts", invoiceProductDTOList);

        return "invoice/purchase-invoice-update";
    }

    /**
     * When end-user click "Save" button, invoice should be updated and they should land on purchase-invoice-list page. When click on Add Product button, this product (InvoiceProduct actually) should be saved to database as an InvoiceProduct, and end-user should be redirected to the very same page with updated Product List section below (Invoice Products actually)
     */
    @PostMapping("/update/{id}")
    public String updateInvoiceProduct(@PathVariable("id")Long id, @ModelAttribute("invoice")InvoiceDTO invoiceToUpdate ){
        InvoiceDTO foundInvoice = invoiceService.findById(id);

        invoiceService.update(foundInvoice, invoiceToUpdate);

        return "redirect:/purchaseInvoices/update/"+id;
    }

    /**
     * When end-user click "Delete" button, invoice should be deleted (soft delete) and they should land on purchase-invoice-list page
     */
    @GetMapping("/delete/{id}")
    public String deleteInvoice(@PathVariable("id")Long invoiceId){

        invoiceService.deleteInvoice(invoiceId);

        return "redirect:/purchaseInvoices/list";
    }

    /**
     * When End-user clicks on the "Approve" button, invoice status should be converted to "Approved" and they should land on purchase-invoice-list page
     */
    @GetMapping("/approve/{id}")
    public String approveInvoice(@PathVariable("id")Long invoiceId){

        invoiceService.approve(invoiceId);

        return "redirect:/purchaseInvoices/list";
    }

    /**
     * When End-User clicks on "Create Purchase Invoice" button, purchase_invoice_create page should be displayed
     */
    @GetMapping("/create")
    public String createInvoice(Model model){
        //invoiceNo differ company to company. In order to auto generate invoiceNo, invoiceCreator() method should know companyTitle
        String loggedInUsername = securityService.getLoggedInUser().getUsername();
        UserDTO loggedInUser = userService.findByUsername(loggedInUsername);
        String companyTitle =  loggedInUser.getCompany().getTitle();

        InvoiceDTO invoice = invoiceService.invoiceCreator(InvoiceType.PURCHASE, companyTitle);

        model.addAttribute("newPurchaseInvoice", invoice);
        model.addAttribute("vendors", List.of(new ClientVendorDTO()));//TODO Vendor should be a dropdown and be populated with only ClientVendors of Type Vendor.

        return "invoice/purchase-invoice-create";
    }

    /**
     * When End-user clicks on SAVE button, a new purchase_invoice should be created in the database and end-user should land the purchase_invoice_update page. (because we only created invoice, but there are no products in it... We need to add them in update page)
     */
    @PostMapping("/create")
    public String createInvoice(@ModelAttribute("newPurchaseInvoice") InvoiceDTO invoice){

        InvoiceDTO createdInvoice = invoiceService.create(invoice);

        return "redirect:/purchaseInvoices/update/"+createdInvoice.getId();
    }

    /**
     * When end-user clicks on "-" button, related invoice_product should be deleted from current Invoice.
     */
    @GetMapping("/removeInvoiceProduct/{invoiceId}/{invoiceProductId}")
    public String removeInvoiceProductFromInvoice(@PathVariable("invoiceId")Long invoiceId, @PathVariable("invoiceProductId")Long invoiceProductId){
        invoiceProductService.removeInvoiceProductFromInvoice(invoiceId, invoiceProductId);

        return "redirect:/purchaseInvoices/update/"+invoiceId;
    }

    /**
     * As a user, I should be able to print approved Purchases Invoices
     */
    @GetMapping("/print/{invoiceId}")
    public String printPurchaseInvoice(@PathVariable("invoiceId")Long invoiceId , Model model){
        InvoiceDTO invoice = invoiceService.findById(invoiceId);
        List<InvoiceProductDTO> invoiceProductDTOList =  invoiceProductService.findByInvoiceId(invoiceId);

        model.addAttribute("invoice", invoice);
        model.addAttribute("company", invoice.getCompany());
        model.addAttribute("invoiceProducts", invoiceProductDTOList);

        return "invoice/invoice_print";
    }

}
