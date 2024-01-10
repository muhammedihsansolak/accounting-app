package com.cydeo.service;

import com.cydeo.dto.ProductDTO;

import java.util.List;

public interface ProductService {

    ProductDTO findById(Long id);

    List<ProductDTO> listAllProducts();

    void save(ProductDTO productDTO);

    void update(ProductDTO productDTO);

    void delete(Long id);
}
