package com.cydeo.controller;

import com.cydeo.dto.CategoryDTO;
import com.cydeo.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public String CategoryList(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "/category/category-list";
    }

    @GetMapping("/create")
    public String createCategory(Model model) {
        model.addAttribute("newCategory", new CategoryDTO());
        return "/category/category-create";

    }

    @PostMapping("/create")
    public String saveCategory(@ModelAttribute("newCategory") CategoryDTO categoryDTO){

        categoryService.save(categoryDTO);
        return "redirect:/categories/list";
    }

    @PostMapping ("/update/{id}")
    public String updateCategory(@PathVariable("id") Long id,@ModelAttribute ("category") CategoryDTO category ){

        categoryService.update(category, id);
        return "redirect:/categories/list";
    }
    @GetMapping("/update/{id}")
    public String editCategory(@PathVariable("id") Long id, Model model){

        model.addAttribute("category", categoryService.findById(id));
        model.addAttribute("products", categoryService.listAllCategories());

        return "category/category-update";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        categoryService.delete(id);
        return "redirect:/categories/list";
    }







}
