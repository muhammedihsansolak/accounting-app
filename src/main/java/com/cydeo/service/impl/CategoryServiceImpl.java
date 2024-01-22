package com.cydeo.service.impl;

import com.cydeo.dto.CategoryDTO;
import com.cydeo.dto.CompanyDTO;
import com.cydeo.dto.ProductDTO;
import com.cydeo.entity.Category;
import com.cydeo.entity.Company;
import com.cydeo.exception.CategoryNotFoundException;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.CategoryRepository;
import com.cydeo.service.CategoryService;
import com.cydeo.service.CompanyService;
import com.cydeo.service.ProductService;
import com.cydeo.service.SecurityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;
    private final ProductService productService;


    public CategoryServiceImpl(CategoryRepository categoryRepository, MapperUtil mapperUtil, CompanyService companyService, SecurityService securityService, ProductService productService) {
        this.categoryRepository = categoryRepository;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
        this.productService = productService;
    }


    @Override
    public CategoryDTO findById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return mapperUtil.convert(category.orElseThrow(() ->
                new CategoryNotFoundException("Category not found with id: " + id)), new CategoryDTO());
    }

    @Override
    public List<CategoryDTO> findAll() {
        List<Category> categoryList = categoryRepository.findAllByIsDeleted(false);
        return categoryList.stream().map(category -> mapperUtil.convert(category, new CategoryDTO())).collect(Collectors.toList());
    }


    @Override
    public List<CategoryDTO> listAllCategories() {
        CompanyDTO companyDTO = securityService.getLoggedInUser().getCompany();
        Company company = mapperUtil.convert(companyDTO, new Company());
        List<Category> categoryList = categoryRepository.findAllByCompanyAndIsDeleted(company, false);

        return categoryList.stream().map(category -> mapperUtil.convert(category, new CategoryDTO())).
                collect(Collectors.toList());
    }


    @Override
    public CategoryDTO save(CategoryDTO dto) {
        Category category = mapperUtil.convert(dto, new Category());
        categoryRepository.save(category);
        return mapperUtil.convert(category, new CategoryDTO());

    }

    @Override
    public CategoryDTO update(CategoryDTO dto, Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));

        Category convertedCategory = mapperUtil.convert(dto, new Category());

        convertedCategory.setId(category.getId());
        convertedCategory.setCompany(category.getCompany());
        Category savedCategory = categoryRepository.save(convertedCategory);
        return mapperUtil.convert(savedCategory, new CategoryDTO());

    }

    @Override
    public boolean isCategoryDescriptionUnique(String description) {
        Category category = categoryRepository.findByDescription(description);
        return category != null;
    }

    @Override
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));

        category.setIsDeleted(Boolean.TRUE);
        categoryRepository.save(category);
    }

    @Override
    public boolean hasProducts(CategoryDTO categoryDTO) {
        List<ProductDTO> products = productService.getProductsByCategory(categoryDTO.getId());
        return !products.isEmpty();

    }

}