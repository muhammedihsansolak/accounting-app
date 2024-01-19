package com.cydeo.service.impl;

import com.cydeo.dto.ProductDTO;

import com.cydeo.entity.Company;
import com.cydeo.entity.Product;


import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.ProductRepository;
import com.cydeo.service.ProductService;
import com.cydeo.service.SecurityService;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;

    public ProductServiceImpl(ProductRepository productRepository, MapperUtil mapperUtil, SecurityService securityService) {
        this.productRepository = productRepository;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
    }

    @Override
    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product can not be found with id: " + id));
        return mapperUtil.convert(product, new ProductDTO());
    }

    @Override
    public List<ProductDTO> listAllProducts() {
        Long companyId = securityService.getLoggedInUser().getCompany().getId();
        List<Product> productList = productRepository.findAllByCompanyId(companyId);
        return productList.stream()
                .map(product -> mapperUtil.convert(product,new ProductDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public void save(ProductDTO productDTO) {

        productRepository.save(mapperUtil.convert(productDTO,new Product()));

    }

    @Override
    public void update(ProductDTO newProduct) {
        Optional<Product> oldProduct = productRepository.findById(newProduct.getId());
        if (oldProduct.isPresent()){
            newProduct.setQuantityInStock(oldProduct.get().getQuantityInStock());
            productRepository.save(mapperUtil.convert(newProduct, new Product()));
        }
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

        return List.of(new ProductDTO());
    }

    @Override
    public BindingResult addProductNameValidation(ProductDTO productDTO, BindingResult bindingResult) {
        Long companyId = securityService.getLoggedInUser().getCompany().getId();
        if (productDTO.getCategory() != null) {
            Long categoryId = productDTO.getCategory().getId();
            // Check if product with the same name exists for the current company
            if (productRepository.existsByNameAndCategory_IdAndCategory_Company_Id(productDTO.getName(),
                    categoryId,companyId)) {
                bindingResult.addError(new FieldError("newProduct", "name",
                        "Product name \"" + productDTO.getName() + "\" is already in use for this company."));
            }
        }
        return bindingResult;
    }
}