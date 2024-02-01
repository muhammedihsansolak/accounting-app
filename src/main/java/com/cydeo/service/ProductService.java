package com.cydeo.service;

import com.cydeo.dto.ProductDTO;
import com.cydeo.entity.Company;
import com.cydeo.enums.ProductUnit;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface ProductService {

    ProductDTO findById(Long id);

    List<ProductDTO> listAllProducts();

    void save(ProductDTO productDTO);

    void update(ProductDTO product);

    void delete(Long id);

    void decreaseProductQuantityInStock(Long id, Integer quantity);

    void increaseProductQuantityInStock(Long id, Integer quantity);

    List<ProductDTO> findProductsByCompanyAndHaveStock(Company company);

    List<ProductDTO> getProductsByCategory(Long id);

    BindingResult addProductNameValidation(ProductDTO productDTO, BindingResult bindingResult);

    BindingResult addUpdateProductNameValidation(ProductDTO product, BindingResult bindingResult);

    void checkProductLowLimitAlert(Long invoiceId);
}