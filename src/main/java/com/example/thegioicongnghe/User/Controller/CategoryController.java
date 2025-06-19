package com.example.thegioicongnghe.User.Controller;

import com.example.thegioicongnghe.Admin.Model.Product;
import com.example.thegioicongnghe.Admin.Model.ProductCategory;
import com.example.thegioicongnghe.Admin.Service.ProductCategoryService;
import com.example.thegioicongnghe.Admin.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private ProductCategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("/{slug}")
    public String viewProductsByCategory(@PathVariable("slug") String slug, Model model) {
        // Lấy danh mục theo slug
        ProductCategory category = categoryService.findCategoryByCategorySlug(slug);
        if (category == null) {
            return "redirect:/404"; // Nếu không tìm thấy, chuyển hướng đến trang 404
        }

        // Lấy danh sách sản phẩm thuộc danh mục
        List<Product> products = productService.findProductsByCategory(category);

        List<Product> allProducts = productService.findAll();

        List<ProductCategory> allCategory = categoryService.findAll();

        // Đưa dữ liệu vào Model
        model.addAttribute("category", category);
        model.addAttribute("products", products);
        model.addAttribute("allProducts", allProducts);
        model.addAttribute("allCategory", allCategory);


        model.addAttribute("content", "user/shop");

        return "layouts/main_layout";    }
}
