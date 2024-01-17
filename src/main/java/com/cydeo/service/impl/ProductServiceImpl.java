package com.cydeo.service.impl;

import com.cydeo.dto.ProductDTO;
import com.cydeo.entity.Category;
import com.cydeo.entity.Product;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.CategoryRepository;
import com.cydeo.repository.ProductRepository;
import com.cydeo.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final MapperUtil mapperUtil;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, MapperUtil mapperUtil, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.mapperUtil = mapperUtil;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product can not be found with id: " + id));
        return mapperUtil.convert(product, new ProductDTO());
    }

    @Override
    public List<ProductDTO> listAllProducts() {
        List<Product> productList = productRepository.findAll();
        return productList.stream()
                .map(product -> mapperUtil.convert(product,new ProductDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public void save(ProductDTO productDTO) {

        productRepository.save(mapperUtil.convert(productDTO,new Product()));

    }

    @Override
    public void update(Long productId, ProductDTO productDtoToBeUpdated) {

        Product productToBeUpdated = productRepository.findById(productId).orElseThrow();
        productToBeUpdated.setId(productToBeUpdated.getId());
        productRepository.save(mapperUtil.convert(productToBeUpdated, new Product()));

    }

    @Override
    public void delete(Long id) {

        Product productToBeDeleted = productRepository.findById(id).get();
        productToBeDeleted.setIsDeleted(true);
        productRepository.save(productToBeDeleted);

    }


    @Override
    public List<ProductDTO> getProductsByCategory(Long id) {

        Category category = categoryRepository.findById(id).orElseThrow();

        List<Product> products = productRepository.findByCategory(category);

        return products.stream().map(product -> mapperUtil.convert(product, new ProductDTO())).collect(Collectors.toList());
    }
}