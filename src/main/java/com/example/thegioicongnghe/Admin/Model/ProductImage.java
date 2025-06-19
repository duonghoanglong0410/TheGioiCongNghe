package com.example.thegioicongnghe.Admin.Model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "ProductImages")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int imageId;

    @Column(nullable = false, length = 255)
    private String imageUrl;  // URL hình ảnh

    @Column(nullable = false)
    private boolean isPrimary = false;
    // Đánh dấu ảnh chính (TRUE cho ảnh chính)

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;  // Sản phẩm tương ứng

    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;  // Thời gian tạo

    // Constructor
    public ProductImage() {
        this.createdAt = new Timestamp(System.currentTimeMillis()); // Đặt thời gian tạo mặc định
    }

    // Getters và Setters
    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
