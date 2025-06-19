package com.example.thegioicongnghe.Admin.Service;

import com.example.thegioicongnghe.Admin.Model.ProductReview;
import com.example.thegioicongnghe.Admin.Repository.ProductReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductReviewService {
    @Autowired
    private ProductReviewRepository productReviewRepository;

    // Lưu đánh giá sản phẩm
    public void saveReview(ProductReview productReview) {
        productReviewRepository.save(productReview);
    }

    // Lấy tất cả đánh giá của sản phẩm
    public List<ProductReview> getReviewsByProductId(int productId) {
        return productReviewRepository.findByProduct_ProductId(productId);
    }

}
