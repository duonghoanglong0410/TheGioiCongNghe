package com.example.thegioicongnghe.Admin.Controller;

import com.example.thegioicongnghe.Admin.Model.ProductCategory;
import com.example.thegioicongnghe.Admin.Service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/admin/categories")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService categoryService;

    private final Path root = Paths.get("uploads/icon_category"); // Thư mục lưu trữ icon


    // Hiển thị danh sách danh mục
    @GetMapping("/list")
    public String listCategories(Model model) {
        List<ProductCategory> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("content", "admin/categories/list"); // Thêm dòng này
        return "layouts/admin_layout"; // Trả về layout với nội dung
    }

    // Trang thêm danh mục mới
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new ProductCategory());
        // Lấy danh sách danh mục cha
        List<ProductCategory> parentCategories = categoryService.findAll();
        model.addAttribute("parentCategories", parentCategories);
        model.addAttribute("content", "admin/categories/add-categories"); // Thêm dòng này
        return "layouts/admin_layout"; // Trả về layout với nội dung
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute ProductCategory category,
                              @RequestParam("iconFile") MultipartFile iconFile) throws IOException {

        String iconName = "default.jpg";
        if (!iconFile.isEmpty()) {
            iconName = iconFile.getOriginalFilename();
            category.setIcon(iconName);

            // Thư mục bên ngoài `resources`
            File uploadsDir = new File("uploads/category_img");

            // Tạo thư mục nếu chưa tồn tại
            if (!uploadsDir.exists()) {
                uploadsDir.mkdirs();
            }

            // Đường dẫn lưu file
            Path path = Paths.get(uploadsDir.getAbsolutePath() + File.separator + iconFile.getOriginalFilename());

            // Lưu file
            Files.copy(iconFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }

        // Lưu category vào database
        categoryService.save(category);
        return "redirect:/admin/categories/list";
    }




    // Trang sửa danh mục
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        ProductCategory category = categoryService.findById(id).orElse(null);
        model.addAttribute("category", category);

        // Lấy danh sách danh mục cha
        List<ProductCategory> parentCategories = categoryService.findAll();
        model.addAttribute("parentCategories", parentCategories);

        model.addAttribute("content", "admin/categories/edit");
        return "layouts/admin_layout";
    }

    // Cập nhật danh mục
    @PostMapping("/edit/{id}")
    public String editCategory(@PathVariable int id,
                               @ModelAttribute ProductCategory category,
                               @RequestParam("iconFile") MultipartFile iconFile,
                               @RequestParam(value = "parentCategory", required = false) Integer parentCategoryId ) throws IOException {

        // Nếu có parentCategoryId, gán nó cho category
        if (parentCategoryId != null) {
            ProductCategory parentCategory = categoryService.findById(parentCategoryId).orElse(null);
            category.setParentCategory(parentCategory);
        }

        // Tìm danh mục cần sửa
        ProductCategory existingCategory = categoryService.findById(id).orElse(null);
        if (existingCategory == null) {
            return "redirect:/admin/categories/list"; // Nếu không tìm thấy, quay về danh sách
        }

        // Cập nhật thông tin
        existingCategory.setCategoryName(category.getCategoryName());
        existingCategory.setCategorySlug(category.getCategorySlug());
        existingCategory.setMetaTitle(category.getMetaTitle());
        existingCategory.setMetaDescription(category.getMetaDescription());
        existingCategory.setParentCategory(category.getParentCategory());

        // Xử lý icon mới
        if (!iconFile.isEmpty()) {
            String iconName = iconFile.getOriginalFilename();

            // Thư mục bên ngoài `resources`
            File uploadsDir = new File("uploads/category_img");

            // Tạo thư mục nếu chưa tồn tại
            if (!uploadsDir.exists()) {
                uploadsDir.mkdirs();
            }

            // Đường dẫn lưu file
            Path path = Paths.get(uploadsDir.getAbsolutePath() + File.separator + iconName);

            // Lưu file
            Files.copy(iconFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // Cập nhật tên file icon
            existingCategory.setIcon(iconName);
        }

        category.setCategoryId(id); // Đảm bảo cập nhật đúng ID
        // Lưu danh mục vào database
        categoryService.save(existingCategory);

        return "redirect:/admin/categories/list";
    }


    // Xóa danh mục
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable int id) {
        categoryService.deleteById(id);
        return "redirect:/admin/categories/list";
    }
}
