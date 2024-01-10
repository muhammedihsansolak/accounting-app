package com.cydeo.service;


import com.cydeo.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    CategoryDTO findById(Long id);
   List<Class<CategoryDTO>> findAll();

void save(CategoryDTO dto);
void update(CategoryDTO dto);
}
