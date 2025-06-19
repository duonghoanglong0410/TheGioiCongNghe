package com.example.thegioicongnghe.Admin.Repository;

import com.example.thegioicongnghe.Admin.Model.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Integer> {
    // Lấy tất cả bình luận của sản phẩm
    List<ProductReview> findByProduct_ProductId(int productId);

    // Lấy tất cả bình luận của người dùng
    List<ProductReview> findByUserId(int userId);
}
