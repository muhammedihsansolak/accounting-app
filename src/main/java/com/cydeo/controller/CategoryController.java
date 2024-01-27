package com.cydeo.controller;

import com.cydeo.dto.CategoryDTO;
import com.cydeo.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public String CategoryList(Model model) {
        model.addAttribute("categories", categoryService.listAllCategories());
        return "/category/category-list";
    }

    @GetMapping("/create")
    public String createCategory(Model model) {
        model.addAttribute("newCategory", new CategoryDTO());
        return "/category/category-create";

    }

    @PostMapping("/create")
    public String saveCategory(@Valid @ModelAttribute("newCategory") CategoryDTO categoryDTO, BindingResult bindingResult){
        boolean categoryDescriptionNotUnique = categoryService.isCategoryDescriptionUnique(categoryDTO.getDescription());
        if (categoryDescriptionNotUnique) {
            bindingResult.rejectValue("description", " ", "This category description already exists");
        }

        if (bindingResult.hasErrors()) {
            return "category/category-create";
        }
        categoryService.save(categoryDTO);
        return "redirect:/categories/list";
    }

    @PostMapping ("/update/{id}")
    public String updateCategory(@Valid @ModelAttribute ("category") CategoryDTO categoryDTO,BindingResult bindingResult,@PathVariable("id") Long id){
        if (categoryService.hasProducts(categoryDTO)){
            bindingResult.rejectValue("description", " ", "This category already has product/products! Make sure the new description that will be provided is proper.");
        }
        if (bindingResult.hasErrors()) {
            return "category/category-update";
        }
        categoryService.update(categoryDTO, id);
        return "redirect:/categories/list";
    }
    @GetMapping("/update/{id}")
    public String editCategory(@PathVariable("id") Long id, Model model){

        model.addAttribute("category", categoryService.findById(id));

        return "category/category-update";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes, @ModelAttribute("category") CategoryDTO categoryDTO) {
        if(categoryService.hasProducts(categoryDTO)){
            redirectAttributes.addFlashAttribute("error", "Can not be deleted! This category has product/products");
            return "redirect:/categories/list";
        }

        categoryService.delete(id);
        return "redirect:/categories/list";
    }







}