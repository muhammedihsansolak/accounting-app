package com.cydeo.service.impl;

import com.cydeo.dto.CompanyDTO;
import com.cydeo.dto.ProductDTO;

import com.cydeo.entity.Company;
import com.cydeo.entity.Product;
import com.cydeo.entity.User;
import com.cydeo.enums.ProductUnit;

import com.cydeo.entity.Category;
import com.cydeo.entity.Product;

import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.CategoryRepository;
import com.cydeo.repository.ProductRepository;
import com.cydeo.service.ProductService;
import com.cydeo.service.SecurityService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final MapperUtil mapperUtil;

    private final SecurityService securityService;
    private final InvoiceProductServiceImpl invoiceProductService;

    public ProductServiceImpl(ProductRepository productRepository, MapperUtil mapperUtil, SecurityService securityService, InvoiceProductServiceImpl invoiceProductService) {
        this.productRepository = productRepository;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
        this.invoiceProductService = invoiceProductService;

    }

    @Override
    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product can not be found with id: " + id));
        return mapperUtil.convert(product, new ProductDTO());
    }

    @Override
    public List<ProductDTO> listAllProducts() {

        CompanyDTO companyDTO = securityService.getLoggedInUser().getCompany();
        Company company = mapperUtil.convert(companyDTO, new Company());

        List<Product> productList = productRepository.findAllByCategory_Company(company);
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
        productDtoToBeUpdated.setId(productToBeUpdated.getId());
        productRepository.save(mapperUtil.convert(productDtoToBeUpdated, new Product()));

    }

    @Override
    public void delete(Long id) {

        Product productToBeDeleted = productRepository.findById(id).get();
        productToBeDeleted.setIsDeleted(true);
        productRepository.save(productToBeDeleted);
    }

    @Override
    public void decreaseProductQuantityInStock(Long id, Integer quantity) {

        Product product = productRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("Product not found with id: " + id));

        int newQuantity = product.getQuantityInStock() - quantity;

        if (newQuantity < 0){
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        product.setQuantityInStock(newQuantity);

        productRepository.save(product);
    }

    @Override
    public List<ProductDTO> findProductsByCompanyAndHaveStock(Company company) {
        List<Product> products = productRepository.findAllByCategory_CompanyAndQuantityInStockGreaterThan(company, 0);

        return products.stream()
                .map(product -> mapperUtil.convert(product, new ProductDTO()))
                .collect(Collectors.toList());
    }


    @Override
    public List<ProductDTO> getProductsByCategory(Long id) {

        Category category = categoryRepository.findById(id).orElseThrow();

        List<Product> products = productRepository.findByCategory(category);

        return products.stream().map(product -> mapperUtil.convert(product, new ProductDTO())).collect(Collectors.toList());
    }
}
