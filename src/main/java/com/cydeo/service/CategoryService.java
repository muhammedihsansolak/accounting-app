package com.cydeo.service;


import com.cydeo.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    CategoryDTO findById(Long id);

    List<CategoryDTO> findAll();

    List<CategoryDTO> listAllCategories();


    CategoryDTO save(CategoryDTO dto);

    CategoryDTO update(CategoryDTO dto, Long id);

    void delete(Long id);

    boolean isCategoryDescriptionUnique(String description);
}
