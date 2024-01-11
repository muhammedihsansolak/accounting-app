package com.cydeo.service;

import com.cydeo.dto.ProductDTO;
import com.cydeo.enums.ProductUnit;

import java.util.List;

public interface ProductService {

    ProductDTO findById(Long id);

    List<ProductDTO> listAllProducts();

    void save(ProductDTO productDTO);

    void update(Long productId, ProductDTO productDtoToBeUpdated);

    void delete(Long id);
}
