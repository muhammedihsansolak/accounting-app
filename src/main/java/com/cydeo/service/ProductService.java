package com.cydeo.service;

import com.cydeo.dto.ProductDTO;
import com.cydeo.entity.Company;
import com.cydeo.enums.ProductUnit;

import java.util.List;

public interface ProductService {

    ProductDTO findById(Long id);

    List<ProductDTO> listAllProducts();

    void save(ProductDTO productDTO);

    void update(ProductDTO product);

    void delete(Long id);

    void decreaseProductQuantityInStock(Long id, Integer quantity);

    List<ProductDTO> findProductsByCompanyAndHaveStock(Company company);

    List<ProductDTO> getProductsByCategory(Long id);
}