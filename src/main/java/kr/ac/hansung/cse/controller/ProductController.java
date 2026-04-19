package kr.ac.hansung.cse.controller;

import kr.ac.hansung.cse.model.Category;
import kr.ac.hansung.cse.model.Product;
import kr.ac.hansung.cse.service.CategoryService;
import kr.ac.hansung.cse.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listProducts(@RequestParam(required = false) String keyword, @RequestParam(required = false) Long categoryId, Model model) {
        List<Product> products;
        if (keyword != null && !keyword.isBlank()) {
            products = productService.searchByName(keyword);
        } else if (categoryId != null) {
            products = productService.searchByCategory(categoryId);
        } else {
            products = productService.getAllProducts();
        }
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        return "productList";
    }

    @GetMapping("/{id}")
    public String viewProduct(@PathVariable("id") Long id, Model model) {
        productService.getProductById(id).ifPresent(p -> model.addAttribute("product", p));
        return "productDetail";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "productCreateForm";
    }

    // ⭐ 에러 절대 안 나는 무적의 데이터 저장 방식 ⭐
    @PostMapping("/create")
    public String createProduct(
            @RequestParam(required = false) Long id,
            @RequestParam String name,
            @RequestParam(required = false) Long category,
            @RequestParam BigDecimal price,
            @RequestParam(required = false) String description) {

        Product p = new Product();
        if (id != null) p.setId(id);
        p.setName(name);
        p.setPrice(price);
        if (description != null) p.setDescription(description);

        // 카테고리 객체 수동 연결
        if (category != null) {
            Category cat = new Category();
            cat.setId(category);
            p.setCategory(cat);
        }

        productService.saveProduct(p);
        return "redirect:/products";
    }

    @GetMapping("/{id}/edit")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        productService.getProductById(id).ifPresent(p -> model.addAttribute("product", p));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "productUpdateForm";
    }

    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}