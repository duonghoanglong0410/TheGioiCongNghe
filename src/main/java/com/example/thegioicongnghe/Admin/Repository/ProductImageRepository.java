package com.example.thegioicongnghe.Admin.Repository;

import com.example.thegioicongnghe.Admin.Model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    // Tìm kiếm dựa trên khóa ngoại (productId) trong ProductImage
    List<ProductImage> findByProduct_ProductId(int productId);
}
