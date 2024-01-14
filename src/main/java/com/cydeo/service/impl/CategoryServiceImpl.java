package com.cydeo.service.impl;

import com.cydeo.dto.CategoryDTO;
import com.cydeo.entity.Category;
import com.cydeo.entity.Company;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.CategoryRepository;
import com.cydeo.service.CategoryService;
import com.cydeo.service.CompanyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final MapperUtil mapperUtil;
    private final CompanyService companyService;


    public CategoryServiceImpl(CategoryRepository categoryRepository, MapperUtil mapperUtil, CompanyService companyService) {
        this.categoryRepository = categoryRepository;
        this.mapperUtil = mapperUtil;
        this.companyService = companyService;
    }


    @Override
    public CategoryDTO findById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return mapperUtil.convert(category, new CategoryDTO());
    }

    @Override
    public List<CategoryDTO> findAll() {
        List<Category> categoryList = categoryRepository.findAllByIsDeleted(false);
        return categoryList.stream().map(category -> mapperUtil.convert(category, new CategoryDTO())).collect(Collectors.toList());
    }



    @Override
    public List<CategoryDTO> listAllCategories() {

        Company company = mapperUtil.convert(companyService.getCompanyDtoByLoggedInUser(), new Company());
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
        Category category = categoryRepository.findById(id).orElseThrow();
        Category convertedCategory = mapperUtil.convert(dto, new Category());
        categoryRepository.save(convertedCategory);
        return mapperUtil.convert(convertedCategory, new CategoryDTO());

    }

    @Override
    public void delete(Long id) {
        Category byId = categoryRepository.findById(id).orElseThrow();
        byId.setIsDeleted(Boolean.TRUE);
        categoryRepository.save(byId);

    }



}
