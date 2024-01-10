package com.cydeo.service;

import com.cydeo.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

 List<CategoryDTO> listAllCategories();
 CategoryDTO getCategoryById(Long id);
 void save(CategoryDTO dto);
 void update(CategoryDTO dto);
 void delete(Long id);


}
