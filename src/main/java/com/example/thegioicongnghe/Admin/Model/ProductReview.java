package com.example.thegioicongnghe.Admin.Model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "ProductReviews")
public class ProductReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;  // Khóa chính cho đánh giá

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;  // Sản phẩm được đánh giá

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserDtls user;  // Người dùng đã thực hiện đánh giá

    @Column(nullable = false)
    private int rating;  // Đánh giá (1 đến 5)

    @Column(columnDefinition = "TEXT")
    private String reviewText;  // Nội dung đánh giá

    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;  // Thời gian tạo

    // Constructor
    public ProductReview() {
        this.createdAt = new Timestamp(System.currentTimeMillis()); // Đặt thời gian tạo mặc định
    }

    // Getters và Setters
    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public UserDtls getUser() {
        return user;
    }

    public void setUser(UserDtls user) {
        this.user = user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
