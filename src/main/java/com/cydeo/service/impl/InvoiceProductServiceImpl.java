package com.cydeo.service.impl;

import com.cydeo.dto.InvoiceDTO;
import com.cydeo.dto.InvoiceProductDTO;
import com.cydeo.entity.Invoice;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.entity.Product;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import com.cydeo.exception.InvoiceProductNotFoundException;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.InvoiceProductRepository;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import com.cydeo.service.ProductService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final InvoiceProductRepository repository;
    private final MapperUtil mapper;
    private final InvoiceService invoiceService;

    public InvoiceProductServiceImpl(InvoiceProductRepository repository, MapperUtil mapper, @Lazy InvoiceService invoiceService) {
        this.repository = repository;
        this.mapper = mapper;
        this.invoiceService = invoiceService;
    }

    @Override
    public InvoiceProductDTO findById(Long id) {
        InvoiceProduct foundInvoiceProduct = repository.findById(id)
                .orElseThrow(() -> new InvoiceProductNotFoundException("InvoiceProduct can not found with id: " + id));

        return mapper.convert(foundInvoiceProduct, new InvoiceProductDTO());
    }

    @Override
    public List<InvoiceProductDTO> findByInvoiceId(Long invoiceId) {
        List<InvoiceProduct> invoiceProductList = repository.findByInvoiceId(invoiceId);
        return invoiceProductList.stream().map(invoiceProduct ->
                mapper.convert(invoiceProduct,new InvoiceProductDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceProductDTO> findByInvoiceIdAndTotalCalculated(Long invoiceId) {
        return findByInvoiceId(invoiceId).stream()
                .map(invoiceProduct -> {
                    BigDecimal taxAmount = invoiceService.calculateTaxForProduct(invoiceProduct);
                    invoiceProduct.setTotal(
                            invoiceProduct.getPrice()
                                    .multiply(BigDecimal.valueOf(invoiceProduct.getQuantity()))
                                    .add(taxAmount));

                    return invoiceProduct;
                }).collect(Collectors.toList());
    }

    @Override
    public InvoiceProductDTO deleteById(Long id) {
        InvoiceProduct invoiceToDelete = repository.findById(id)
                .orElseThrow(() -> new InvoiceProductNotFoundException("InvoiceProduct can not found with id: " + id));

        invoiceToDelete.setIsDeleted(Boolean.TRUE);

        InvoiceProduct deleted = repository.save(invoiceToDelete);
        return mapper.convert(deleted, new InvoiceProductDTO());
    }

    @Override
    public void removeInvoiceProductFromInvoice(Long invoiceId, Long invoiceProductId) {
        List<InvoiceProductDTO> invoiceProductDTOList = findByInvoiceId(invoiceId);

        invoiceProductDTOList.stream()
                .filter(invoiceProductDTO -> invoiceProductDTO.getId() == invoiceProductId)
                .forEach(invoiceProductDTO -> deleteById(invoiceProductDTO.getId())); //soft delete
    }

    @Override
    public InvoiceProductDTO create(InvoiceProductDTO invoiceProductDTO, Long invoiceId) {
        InvoiceDTO invoiceDTO = invoiceService.findById(invoiceId);

        invoiceProductDTO.setInvoice(invoiceDTO);

        InvoiceProduct invoiceProduct = mapper.convert(invoiceProductDTO, new InvoiceProduct());
        invoiceProduct.setId(null);//bug fix
        InvoiceProduct savedInvoice = repository.save(invoiceProduct);

        return mapper.convert(savedInvoice, new InvoiceProductDTO());
    }

    @Override
    public BindingResult doesProductHaveEnoughStock(InvoiceProductDTO invoiceProductDTO, BindingResult bindingResult) {
        if (invoiceProductDTO.getProduct() != null) {
            if (invoiceProductDTO.getProduct().getQuantityInStock() != null) {
                if (invoiceProductDTO.getQuantity() != null) {
                    Integer invoiceProductQuantity = invoiceProductDTO.getQuantity();
                    Integer quantityInStock = invoiceProductDTO.getProduct().getQuantityInStock();
                    if (quantityInStock < invoiceProductQuantity){
                        ObjectError error = new FieldError("newInvoiceProduct", "product", "Product " + invoiceProductDTO.getProduct().getName() + " has no enough stock!");
                        bindingResult.addError(error);
                    }
                }
            }
        }
        return bindingResult;
    }

    @Override
    public boolean doesProductHasInvoice(Long productId) {
        return repository.existsByProductId(productId);
    }

    @Override
    public void deleteByInvoice(InvoiceDTO invoice) {
        List<InvoiceProduct> products = repository.findByInvoiceId(invoice.getId());
        products.forEach(invoiceProduct -> invoiceProduct.setIsDeleted(true));
        repository.saveAll(products);
    }

    @Override
    public List<InvoiceProductDTO> findAllApprovedInvoiceInvoiceProduct(InvoiceStatus invoiceStatus) {
        return repository.findAllByInvoice_InvoiceStatus(invoiceStatus).stream()
                .map(invoiceProduct -> mapper.convert(invoiceProduct,new InvoiceProductDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public void save(InvoiceProductDTO invoiceProductDTO) {
        repository.save(mapper.convert(invoiceProductDTO, new InvoiceProduct()));
    }

    @Override
    public List<InvoiceProductDTO> getPerchesInvoiceProductsListQuantityNotZero(
            String companyName, String productName, InvoiceType invoiceType, int quantity) {
         return repository
                .findByInvoice_Company_TitleAndProduct_NameAndInvoice_InvoiceTypeAndRemainingQuantityGreaterThanOrderByInsertDateTimeAsc(
                        companyName, productName, invoiceType,quantity)
                .stream().map(invPro->mapper.convert(invPro,new InvoiceProductDTO()))
                 .collect(Collectors.toList());
    }
}
