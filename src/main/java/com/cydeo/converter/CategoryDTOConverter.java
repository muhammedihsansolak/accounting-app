package com.cydeo.converter;

import com.cydeo.dto.CategoryDTO;
import com.cydeo.entity.Category;
import com.cydeo.service.CategoryService;
import org.springframework.stereotype.Component;

@Component
public class CategoryDTOConverter {

    CategoryService categoryService;

    public CategoryDTOConverter(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public CategoryDTO convert(Long id) {
        if (id==null || id.equals("")){
            return null;
        }

        return categoryService.findById(id);

    }


}

