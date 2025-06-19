package com.example.thegioicongnghe.Admin.Repository;

import com.example.thegioicongnghe.Admin.Model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
    // Thay đổi slug thành categorySlug
    boolean existsByCategorySlug(String categorySlug);

    ProductCategory findByCategorySlug(String slug);
}
