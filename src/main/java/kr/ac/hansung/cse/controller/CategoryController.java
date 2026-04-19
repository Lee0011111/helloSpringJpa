package kr.ac.hansung.cse.controller;

import jakarta.validation.Valid;
import kr.ac.hansung.cse.model.Category;
import kr.ac.hansung.cse.model.CategoryForm;
import kr.ac.hansung.cse.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private ProductService productService;

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("categoryForm", new CategoryForm());
        return "categoryCreateForm";
    }

    @PostMapping("/create")
    public String createCategory(@Valid CategoryForm categoryForm, BindingResult result) {
        if (result.hasErrors()) {
            return "categoryCreateForm";
        }
        Category category = new Category();
        category.setName(categoryForm.getName());
        productService.saveCategory(category);
        return "redirect:/products";
    }
}