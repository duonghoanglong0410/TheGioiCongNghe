package com.example.thegioicongnghe.Admin.Service;

import com.example.thegioicongnghe.Admin.Model.ProductCategory;
import com.example.thegioicongnghe.Admin.Repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoryService {

    @Autowired
    private ProductCategoryRepository categoryRepository;

    public List<ProductCategory> findAll() {
        return categoryRepository.findAll();
    }


    //Hiển thị sản phẩm theo danh mục
    public ProductCategory findCategoryByCategorySlug(String slug) {
        return categoryRepository.findByCategorySlug(slug);
    }

    public Optional<ProductCategory> findById(int id) {
        return categoryRepository.findById(id);
    }
    public String generateUniqueSlug(String title) {
        String baseSlug = SlugUtils.toSlug(title); // Tạo slug cơ bản từ tiêu đề
        String categorySlug = baseSlug;
        int count = 1;

        // Kiểm tra xem slug đã tồn tại trong cơ sở dữ liệu chưa
        while (categoryRepository.existsByCategorySlug(categorySlug)) {
            categorySlug = baseSlug + "-" + count; // Thêm số đằng sau
            count++;
        }

        return categorySlug;
    }

    public ProductCategory save(ProductCategory category) {
        // Tạo slug duy nhất
        String uniqueSlug = generateUniqueSlug(category.getCategoryName());
        category.setCategorySlug(uniqueSlug);
        return categoryRepository.save(category);
    }

    public void deleteById(int id) {
        categoryRepository.deleteById(id);
    }
}
