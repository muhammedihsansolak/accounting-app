package com.cydeo.service.impl;

import com.cydeo.dto.ProductDTO;
import com.cydeo.entity.Product;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.ProductRepository;
import com.cydeo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final MapperUtil mapperUtil;

    public ProductServiceImpl(ProductRepository productRepository, MapperUtil mapperUtil) {
        this.productRepository = productRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public ProductDTO findById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return mapperUtil.convert(product, new ProductDTO());
    }

    @Override
    public List<ProductDTO> listAllProducts() {
        List<Product> productList = productRepository.findAllByIsDeleted(false);
        return productList.stream()
                .map(product -> mapperUtil.convert(product,new ProductDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public void save(ProductDTO productDTO) {

    }

    @Override
    public void update(ProductDTO productDTO) {

    }

    @Override
    public void delete(ProductDTO productDTO) {

    }
}
