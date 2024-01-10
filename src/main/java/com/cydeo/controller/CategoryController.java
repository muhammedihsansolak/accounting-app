package com.cydeo.controller;

import com.cydeo.dto.CategoryDTO;
import com.cydeo.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/category-list")
    public String CategoryList(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "/category/category-list";
    }

    @GetMapping("/category-create")
    public String createCategory(Model model) {
        model.addAttribute("categories", new CategoryDTO());
        return "/category/category-create";

    }

    @PostMapping("/category-create")
    public String createCategory(@ModelAttribute("categories") CategoryDTO category){
        categoryService.save(category);
        return "/category/category-create";
    }

    @PostMapping ("/category-update")
    public String updateCategory(@ModelAttribute("category") CategoryDTO category){
        categoryService.update(category);
        return "/category/category-list";
    }







}
